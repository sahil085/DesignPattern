import { createStore, applyMiddleware, compose } from 'redux';
import thunkMiddleware from 'redux-thunk';
import {createLogger} from 'redux-logger';
import rootReducer from '../reducers';
import PromiseMiddleware from '../config/promiseMiddleware'

const loggerMiddleware = createLogger() ;

export default function configureStore() {
 // const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose; // add support for Redux dev tools
  return createStore(rootReducer, compose(
          applyMiddleware(PromiseMiddleware,
              thunkMiddleware,
              loggerMiddleware)
              //this for browser debugging of redux
              //also add dev tools in browser for redux
              ,window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
  )
);
}
