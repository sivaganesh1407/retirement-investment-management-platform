const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

function getToken() {
  return localStorage.getItem('token')
}

export async function api(path, options = {}) {
  const url = path.startsWith('http') ? path : `${BASE}${path}`
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  }
  const token = getToken()
  if (token) headers['Authorization'] = `Bearer ${token}`
  const res = await fetch(url, { ...options, headers })
  const text = await res.text()
  const data = text ? JSON.parse(text) : null
  if (!res.ok) throw new Error(data?.message || res.statusText)
  return data
}

export function login(email, password) {
  return api('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
  })
}

export function register(name, email, password, role = 'CUSTOMER') {
  return api('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ name, email, password, role }),
  })
}

export function getCustomers() {
  return api('/customers')
}

export function createCustomer(customer) {
  return api('/customers', {
    method: 'POST',
    body: JSON.stringify(customer),
  })
}

export function getPortfoliosByCustomer(customerId) {
  return api(`/portfolio/customer/${customerId}`)
}

export function createPortfolio(customerId, portfolioName) {
  return api('/portfolio', {
    method: 'POST',
    body: JSON.stringify({ customerId, portfolioName }),
  })
}

export function getInvestments(portfolioId) {
  return api(`/portfolio/${portfolioId}/investments`)
}

export function addInvestment(portfolioId, investment) {
  return api(`/portfolio/${portfolioId}/investments`, {
    method: 'POST',
    body: JSON.stringify(investment),
  })
}

export function getProjection(customerId) {
  return api(`/retirement/projection/${customerId}`)
}

export function setToken(token) {
  if (token) localStorage.setItem('token', token)
  else localStorage.removeItem('token')
}

export function isAuthenticated() {
  return !!getToken()
}
