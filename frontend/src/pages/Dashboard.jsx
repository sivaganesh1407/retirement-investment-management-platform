import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { getCustomers, createCustomer } from '../api'

export default function Dashboard({ onLogout }) {
  const [customers, setCustomers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({
    name: '',
    email: '',
    retirementGoal: '',
    riskProfile: 'MODERATE',
  })

  useEffect(() => {
    load()
  }, [])

  async function load() {
    try {
      const data = await getCustomers()
      setCustomers(Array.isArray(data) ? data : [])
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleCreate(e) {
    e.preventDefault()
    setError('')
    try {
      await createCustomer({
        name: form.name,
        email: form.email,
        retirementGoal: form.retirementGoal ? Number(form.retirementGoal) : 0,
        riskProfile: form.riskProfile,
      })
      setForm({ name: '', email: '', retirementGoal: '', riskProfile: 'MODERATE' })
      setShowForm(false)
      load()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Customers</h1>
        <button type="button" className="btn btn-secondary" onClick={onLogout}>
          Logout
        </button>
      </header>
      {error && <p className="error">{error}</p>}
      {loading ? (
        <p>Loading customers…</p>
      ) : (
        <>
          <button
            type="button"
            className="btn btn-primary"
            onClick={() => setShowForm((v) => !v)}
          >
            {showForm ? 'Cancel' : 'Add customer'}
          </button>
          {showForm && (
            <form className="card form-card" onSubmit={handleCreate}>
              <h3>New customer</h3>
              <input
                placeholder="Name"
                value={form.name}
                onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
                required
              />
              <input
                type="email"
                placeholder="Email"
                value={form.email}
                onChange={(e) => setForm((f) => ({ ...f, email: e.target.value }))}
                required
              />
              <input
                type="number"
                placeholder="Retirement goal"
                value={form.retirementGoal}
                onChange={(e) => setForm((f) => ({ ...f, retirementGoal: e.target.value }))}
              />
              <select
                value={form.riskProfile}
                onChange={(e) => setForm((f) => ({ ...f, riskProfile: e.target.value }))}
              >
                <option value="CONSERVATIVE">Conservative</option>
                <option value="MODERATE">Moderate</option>
                <option value="AGGRESSIVE">Aggressive</option>
              </select>
              <button type="submit" className="btn btn-primary">Create</button>
            </form>
          )}
          <ul className="customer-list">
            {customers.map((c) => (
              <li key={c.id} className="card customer-card">
                <div>
                  <strong>{c.name}</strong> — {c.email}
                </div>
                <div className="meta">
                  Goal: ${Number(c.retirementGoal || 0).toLocaleString()} · {c.riskProfile}
                </div>
                <Link to={`/customer/${c.id}`} className="btn btn-small">
                  View portfolios & projection
                </Link>
              </li>
            ))}
          </ul>
          {customers.length === 0 && !showForm && (
            <p className="muted">No customers yet. Add one above.</p>
          )}
        </>
      )}
    </div>
  )
}
