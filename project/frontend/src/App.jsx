
import './App.css'
import Navbar from './components/Navbar'
import CampusInfo from './pages/CampusInfo'
import Footer from './components/Footer'
import BackToTop from './components/BackToTop'
function App() {
  return (
    <div className="app">
      <Navbar />

      <main id="home" className="hero">
        <h1>Your Campus, Your Community.</h1>

        <p>
          Connect with students, exchange skills, discover campus
          resources, and make university life easier.
        </p>

        <button>Explore CompusConnect</button>
      </main>
      <CampusInfo />
      <section id="about" className="about-section">
  <h2>About CompusConnect</h2>
  <p>
    CompusConnect is designed to bring students together,
    make campus resources easier to discover, and encourage
    collaboration through sharing skills and opportunities.
  </p>
</section>
<BackToTop />
<Footer />
    </div>
  )
}

export default App


