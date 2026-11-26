function About() {
  return (
    <section id="about" className="about-section">
      <span className="about-label">OUR PURPOSE</span>

      <h2>More Than Just a Campus Platform</h2>

      <p className="about-intro">
        University life is full of opportunities, but finding
        the right resources and people is not always easy.
        CompusConnect brings these experiences together in one place.
      </p>

      <div className="about-values">
        <article className="about-value">
          <span>🔎</span>
          <h3>Discover</h3>
          <p>
            Explore campus facilities, services, and useful resources.
          </p>
        </article>

        <article className="about-value">
          <span>🌐</span>
          <h3>Connect</h3>
          <p>
            Build connections and collaborate with fellow students.
          </p>
        </article>

        <article className="about-value">
          <span>🎓</span>
          <h3>Share</h3>
          <p>
            Exchange skills, knowledge, and opportunities.
          </p>
        </article>
      </div>
    </section>
  )
}

export default About