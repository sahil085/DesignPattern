import { combineReducers } from 'redux'
import dashboardReducer from './dashboardReducer'
import clientReducer from './clientReducer'

const rootReducer = combineReducers({
  dashboard : dashboardReducer,
  client : clientReducer
})

export default rootReducer
