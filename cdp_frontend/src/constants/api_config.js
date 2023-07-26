const BACKEND_HOST = process.env.REACT_APP_BACKEND_HOST || 'localhost'
const URL_DOMAIN = "http://" + BACKEND_HOST + ":8280"
export const DOMAIN = URL_DOMAIN
export const SEGMENT = "/api/v1/segment"
export const SEGMENT_PAGE = "/api/v1/page/segment/"
export const CUSTOMER = "/api/v1/customer/"