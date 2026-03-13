import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import {
  getPortfoliosByCustomer,
  createPortfolio,
  getInvestments,
  addInvestment,
  getProjection,
} from '../api'

export default function CustomerDetail({ onLogout }) {
  const { customerId } = useParams()
  const [portfolios, setPortfolios] = useState([])
  const [projection, setProjection] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showPortfolioForm, setShowPortfolioForm] = useState(false)
  const [portfolioName, setPortfolioName] = useState('')
  const [selectedPortfolio, setSelectedPortfolio] = useState(null)
  const [investments, setInvestments] = useState([])
  const [showInvestmentForm, setShowInvestmentForm] = useState(false)
  const [invForm, setInvForm] = useState({
    assetName: '',
    assetType: 'ETF',
    amount: '',
    purchaseDate: new Date().toISOString().slice(0, 10),
  })

  useEffect(() => {
    load()
  }, [customerId])

  async function load() {
    setError('')
    try {
      const [portData, projData] = await Promise.all([
        getPortfoliosByCustomer(customerId),
        getProjection(customerId),
      ])
      setPortfolios(Array.isArray(portData) ? portData : [])
      setProjection(projData)
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  async function loadInvestments(portfolioId) {
    try {
      const data = await getInvestments(portfolioId)
      setInvestments(Array.isArray(data) ? data : [])
      setSelectedPortfolio(portfolioId)
      setShowInvestmentForm(false)
    } catch (e) {
      setError(e.message)
    }
  }

  async function handleCreatePortfolio(e) {
    e.preventDefault()
    setError('')
    try {
      await createPortfolio(Number(customerId), portfolioName)
      setPortfolioName('')
      setShowPortfolioForm(false)
      load()
    } catch (e) {
      setError(e.message)
    }
  }

  async function handleAddInvestment(e) {
    e.preventDefault()
    if (!selectedPortfolio) return
    setError('')
    try {
      await addInvestment(selectedPortfolio, {
        assetName: invForm.assetName,
        assetType: invForm.assetType,
        amount: Number(invForm.amount),
        purchaseDate: invForm.purchaseDate,
      })
      setInvForm({
        assetName: '',
        assetType: 'ETF',
        amount: '',
        purchaseDate: new Date().toISOString().slice(0, 10),
      })
      setShowInvestmentForm(false)
      loadInvestments(selectedPortfolio)
      load()
    } catch (e) {
      setError(e.message)
    }
  }

  if (loading) return <div className="page-loading">Loading…</div>

  return (
    <div className="customer-detail">
      <header className="dashboard-header">
        <Link to="/" className="back">← Back to customers</Link>
        <button type="button" className="btn btn-secondary" onClick={onLogout}>
          Logout
        </button>
      </header>
      {error && <p className="error">{error}</p>}

      {projection && (
        <section className="card projection-card">
          <h2>Retirement projection</h2>
          <p><strong>Current savings:</strong> ${Number(projection.currentSavings || 0).toLocaleString()}</p>
          <p><strong>Projected value at 65:</strong> ${Number(projection.projectedValue || 0).toLocaleString()}</p>
          <p><strong>Retirement age:</strong> {projection.retirementAge ?? 65}</p>
        </section>
      )}

      <section>
        <h2>Portfolios</h2>
        <button
          type="button"
          className="btn btn-primary"
          onClick={() => setShowPortfolioForm((v) => !v)}
        >
          {showPortfolioForm ? 'Cancel' : 'Add portfolio'}
        </button>
        {showPortfolioForm && (
          <form className="card form-card" onSubmit={handleCreatePortfolio}>
            <input
              placeholder="Portfolio name"
              value={portfolioName}
              onChange={(e) => setPortfolioName(e.target.value)}
              required
            />
            <button type="submit" className="btn btn-primary">Create</button>
          </form>
        )}
        <ul className="portfolio-list">
          {portfolios.map((p) => (
            <li key={p.id} className="card">
              <div>
                <strong>{p.portfolioName}</strong>
                <button
                  type="button"
                  className="btn btn-small"
                  onClick={() => loadInvestments(p.id)}
                >
                  {selectedPortfolio === p.id ? 'Hide investments' : 'View investments'}
                </button>
              </div>
              {selectedPortfolio === p.id && (
                <>
                  <button
                    type="button"
                    className="btn btn-small"
                    onClick={() => setShowInvestmentForm((v) => !v)}
                  >
                    {showInvestmentForm ? 'Cancel' : 'Add investment'}
                  </button>
                  {showInvestmentForm && (
                    <form className="form-inline" onSubmit={handleAddInvestment}>
                      <input
                        placeholder="Asset name"
                        value={invForm.assetName}
                        onChange={(e) => setInvForm((f) => ({ ...f, assetName: e.target.value }))}
                        required
                      />
                      <select
                        value={invForm.assetType}
                        onChange={(e) => setInvForm((f) => ({ ...f, assetType: e.target.value }))}
                      >
                        <option value="STOCK">Stock</option>
                        <option value="ETF">ETF</option>
                        <option value="BOND">Bond</option>
                        <option value="MUTUAL_FUND">Mutual fund</option>
                      </select>
                      <input
                        type="number"
                        placeholder="Amount"
                        value={invForm.amount}
                        onChange={(e) => setInvForm((f) => ({ ...f, amount: e.target.value }))}
                        required
                      />
                      <input
                        type="date"
                        value={invForm.purchaseDate}
                        onChange={(e) => setInvForm((f) => ({ ...f, purchaseDate: e.target.value }))}
                      />
                      <button type="submit" className="btn btn-primary">Add</button>
                    </form>
                  )}
                  <ul className="investment-list">
                    {investments.map((inv) => (
                      <li key={inv.id}>
                        {inv.assetName} — {inv.assetType} — ${Number(inv.amount || 0).toLocaleString()} ({inv.purchaseDate})
                      </li>
                    ))}
                  </ul>
                </>
              )}
            </li>
          ))}
        </ul>
      </section>
    </div>
  )
}
