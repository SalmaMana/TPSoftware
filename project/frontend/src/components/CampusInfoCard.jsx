function CampusInfoCard({
  title,
  description,
  category,
  location,
  openingHours,
  onClick,
}) {
  return (
    <article
      className="campus-card"
      onClick={onClick}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault()
          onClick()
        }
      }}
      tabIndex={0}
      role="button"
      aria-label={`View details about ${title}`}
    >
      <span className="campus-category">{category}</span>

      <h3>{title}</h3>

      <p>{description}</p>

      <div className="campus-details">
        <p>📍 {location}</p>
        <p>🕒 {openingHours}</p>
      </div>

      <span className="card-action">View details →</span>
    </article>
  )
}

export default CampusInfoCard