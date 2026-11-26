// Get all campus information from the backend
export async function getAllCampusInfo() {
  const response = await fetch('/api/campus-info')

  if (!response.ok) {
    throw new Error('Failed to fetch campus information')
  }

  return await response.json()
}