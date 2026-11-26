import './App.css'

import Navbar from './components/Navbar'
import Home from './pages/Home'
import CampusInfo from './pages/CampusInfo'
import About from './pages/About'
import Footer from './components/Footer'
import BackToTop from './components/BackToTop'

function App() {
  return (
    <div className="app">
      <Navbar />

      <main id="main-content">
        <Home />

        <CampusInfo />

        <About />
      </main>

      <BackToTop />
      <Footer />
    </div>
  )
}

export default App