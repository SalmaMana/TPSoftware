
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

  useEffect(() => {
    localStorage.setItem('campusFavorites', JSON.stringify(favorites))
  }, [favorites])

  async function loadCampusInfo() {
    setLoading(true)
    setError(null)

    try {
      const data = await getAllCampusInfo()
      setCampusItems(data)
    } catch {
      setError('We could not load campus information. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadCampusInfo()
  }, [])

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

  useEffect(() => {
    if (!selectedItem) return

    const previousOverflow = document.body.style.overflow
    document.body.style.overflow = 'hidden'

    return () => {
      document.body.style.overflow = previousOverflow
    }
  }, [selectedItem])

  if (loading) {
    return (
      <section className="campus-section campus-status">
        <div className="loading-spinner"></div>
        <h2>Discovering your campus...</h2>
        <p>We're gathering campus services and resources for you.</p>
      </section>
    )
  }

  if (error) {
    return (
      <section className="campus-section campus-status">
        <div className="error-icon">!</div>
        <h2>Something went wrong</h2>
        <p>{error}</p>
        <button className="retry-button" onClick={loadCampusInfo}>
          Try Again
        </button>
      </section>
    )
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
        <div className="empty-state">
          <div className="empty-state-icon">⌕</div>

          <h3>
            {showFavoritesOnly && favorites.length === 0
              ? 'No favorites yet'
              : showFavoritesOnly
                ? 'No matching favorites'
                : 'No results found'}
          </h3>

          <p>
            {showFavoritesOnly && favorites.length === 0
              ? 'Save campus facilities you like by clicking the favorite button on a card.'
              : showFavoritesOnly
                ? 'Your saved favorites do not match your current search or category. Try changing your filters.'
                : 'We could not find any campus facilities matching your search. Try a different keyword or category.'}
          </p>

          <button
            className="empty-state-button"
            onClick={() => {
              setSearchTerm('')
              setSelectedCategory('ALL')
              setShowFavoritesOnly(false)
            }}
          >
            Reset filters
          </button>
        </div>
      )}

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
