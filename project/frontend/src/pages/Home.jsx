function Home() {
  return (
    <>
      <section id="home" className="hero">
        <span className="hero-label">WELCOME TO COMPUSCONNECT</span>

        <h1>
          Your Campus.
          <br />
          Your Community.
        </h1>

        <p>
          Discover your campus, connect with students,
          exchange skills, and make university life easier.
        </p>

        <div className="hero-actions">
          <a href="#campus" className="hero-button">
            Explore Campus
          </a>

          <a href="#about" className="hero-secondary-button">
            Learn More
          </a>
        </div>
      </section>

      <section className="highlights-section">
        <div className="highlight-item">
          <span className="highlight-icon">📍</span>
          <h3>Campus Resources</h3>
          <p>Find useful places and services around campus.</p>
        </div>

        <div className="highlight-item">
          <span className="highlight-icon">🤝</span>
          <h3>Student Connections</h3>
          <p>Discover opportunities to connect and collaborate.</p>
        </div>

        <div className="highlight-item">
          <span className="highlight-icon">💡</span>
          <h3>Skill Sharing</h3>
          <p>Learn from others and share what you know.</p>
        </div>
      </section>
    </>
  )
}

export default Home