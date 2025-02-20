import defaultSettings from '@/settings'

const title = defaultSettings.title || 'Lucky Admin Vue'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
