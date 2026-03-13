import { useState, useEffect } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { isAuthenticated, setToken } from './api'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import CustomerDetail from './pages/CustomerDetail'

export default function App() {
  const [auth, setAuth] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    setAuth(isAuthenticated())
    setLoading(false)
  }, [])

  function onLogin(token) {
    setToken(token)
    setAuth(true)
  }

  function onLogout() {
    setToken(null)
    setAuth(false)
  }

  if (loading) return <div className="app-loading">Loading…</div>

  return (
    <div className="app">
      <Routes>
        <Route path="/login" element={
          auth ? <Navigate to="/" replace /> : <Login onLogin={onLogin} />
        } />
        <Route path="/" element={
          auth ? <Dashboard onLogout={onLogout} /> : <Navigate to="/login" replace />
        } />
        <Route path="/customer/:customerId" element={
          auth ? <CustomerDetail onLogout={onLogout} /> : <Navigate to="/login" replace />
        } />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </div>
  )
}
