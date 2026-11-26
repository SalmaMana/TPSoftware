function Navbar() {
  return (
    <header className="navbar">
      <a href="#main-content" className="skip-link">
        Skip to content
      </a>

      <a href="#home" className="navbar-brand" aria-label="CompusConnect home">
        <h2>CompusConnect</h2>
      </a>

      <nav aria-label="Main navigation">
        <a href="#home">Home</a>
        <a href="#campus">Campus</a>
        <a href="#about">About</a>
      </nav>
    </header>
  )
}

export default Navbar