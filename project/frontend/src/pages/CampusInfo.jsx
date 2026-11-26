
import { useEffect, useState } from 'react'
import CampusInfoCard from '../components/CampusInfoCard'
import { getAllCampusInfo } from '../services/campusInfoService'

function CampusInfo() {
  const [campusItems, setCampusItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [selectedCategory, setSelectedCategory] = useState('ALL')
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedItem, setSelectedItem] = useState(null)

  // Load favorites from localStorage when the component initializes
  const [favorites, setFavorites] = useState(() => {
    try {
      const savedFavorites = localStorage.getItem('campusFavorites')
      return savedFavorites ? JSON.parse(savedFavorites) : []
    } catch {
      return []
    }
  })

  const [showFavoritesOnly, setShowFavoritesOnly] = useState(false)

  const categories = [
    'ALL',
    'STUDY_SPACES',
    'FOOD',
    'TRANSPORT',
    'ADMINISTRATION',
    'SPORT',
    'HEALTH',
    'EVENTS',
    'OTHER',
  ]

  const filteredItems = campusItems.filter((item) => {
    const matchesCategory =
      selectedCategory === 'ALL' || item.category === selectedCategory

    const matchesSearch =
      item.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.location.toLowerCase().includes(searchTerm.toLowerCase())

    const matchesFavorites =
      !showFavoritesOnly || favorites.includes(item.id)

    return matchesCategory && matchesSearch && matchesFavorites
  })

  function toggleFavorite(itemId) {
    setFavorites((previousFavorites) =>
      previousFavorites.includes(itemId)
        ? previousFavorites.filter((id) => id !== itemId)
        : [...previousFavorites, itemId],
    )
  }

  // Save favorites whenever the favorites state changes
  useEffect(() => {
    localStorage.setItem('campusFavorites', JSON.stringify(favorites))
  }, [favorites])

  // Fetch campus information
  useEffect(() => {
    async function loadCampusInfo() {
      try {
        const data = await getAllCampusInfo()
        setCampusItems(data)
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    loadCampusInfo()
  }, [])

  // Close the modal with the Escape key
  useEffect(() => {
    if (!selectedItem) return

    function handleKeyDown(event) {
      if (event.key === 'Escape') {
        setSelectedItem(null)
      }
    }

    document.addEventListener('keydown', handleKeyDown)

    return () => {
      document.removeEventListener('keydown', handleKeyDown)
    }
  }, [selectedItem])

  // Prevent background scrolling while the modal is open
  useEffect(() => {
    if (!selectedItem) return

    const previousOverflow = document.body.style.overflow
    document.body.style.overflow = 'hidden'

    return () => {
      document.body.style.overflow = previousOverflow
    }
  }, [selectedItem])

  if (loading) {
    return <p>Loading campus information...</p>
  }

  if (error) {
    return <p>Error: {error}</p>
  }

  return (
    <section id="campus" className="campus-section">
      <h2>Explore Your Campus</h2>

      <p>
        Discover the services, facilities, and resources
        available at your university.
      </p>

      <div className="campus-search">
        <input
          type="text"
          placeholder="Search campus facilities..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />

        {searchTerm && (
          <button
            className="clear-search"
            onClick={() => setSearchTerm('')}
          >
            Clear
          </button>
        )}
      </div>

      <div className="category-filters">
        {categories.map((category) => (
          <button
            key={category}
            onClick={() => setSelectedCategory(category)}
            className={selectedCategory === category ? 'active' : ''}
          >
            {category === 'ALL'
              ? 'All'
              : category
                  .split('_')
                  .map((word) => word.charAt(0) + word.slice(1).toLowerCase())
                  .join(' ')}
          </button>
        ))}
      </div>

      {/* Favorites filter */}
      <div className="favorites-filter">
        <button
          className={showFavoritesOnly ? 'favorites-active' : ''}
          onClick={() => setShowFavoritesOnly(!showFavoritesOnly)}
        >
          {showFavoritesOnly ? '♥ Showing Favorites' : '♡ Show Favorites'}
        </button>
      </div>

      <p className="results-count">
        {filteredItems.length} campus items found
      </p>

      {filteredItems.length > 0 ? (
        <div className="campus-content">
          {filteredItems.map((item) => (
            <div className="campus-card-wrapper" key={item.id}>
              <CampusInfoCard
                title={item.title}
                description={item.description}
                category={item.category}
                location={item.location}
                openingHours={item.openingHours}
                onClick={() => setSelectedItem(item)}
              />

              <button
                className={`favorite-button ${
                  favorites.includes(item.id) ? 'is-favorite' : ''
                }`}
                onClick={() => toggleFavorite(item.id)}
                aria-label={
                  favorites.includes(item.id)
                    ? `Remove ${item.title} from favorites`
                    : `Add ${item.title} to favorites`
                }
              >
                {favorites.includes(item.id)
                  ? '♥ Remove from favorites'
                  : '♡ Add to favorites'}
              </button>
            </div>
          ))}
        </div>
      ) : (
        <p className="no-results">
          {showFavoritesOnly
            ? 'You have no matching favorite campus items.'
            : 'No campus information matches your search.'}
        </p>
      )}

      {/* Campus information modal */}
      {selectedItem && (
        <div
          className="modal-overlay"
          onClick={(e) => {
            if (e.target === e.currentTarget) {
              setSelectedItem(null)
            }
          }}
        >
          <div
            className="campus-modal"
            role="dialog"
            aria-modal="true"
            aria-labelledby="modal-title"
          >
            <button
              className="modal-close"
              onClick={() => setSelectedItem(null)}
              aria-label="Close details"
            >
              &times;
            </button>

            <span className="campus-category">
              {selectedItem.category}
            </span>

            <h3 id="modal-title">{selectedItem.title}</h3>

            <p className="modal-description">
              {selectedItem.description}
            </p>

            <div className="modal-details">
              <p>
                <strong>Location:</strong> {selectedItem.location}
              </p>

              <p>
                <strong>Opening hours:</strong> {selectedItem.openingHours}
              </p>
            </div>

            <button
              className="modal-action"
              onClick={() => setSelectedItem(null)}
            >
              Done
            </button>
          </div>
        </div>
      )}
    </section>
  )
}

export default CampusInfo
