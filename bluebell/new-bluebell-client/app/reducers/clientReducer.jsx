import Immutable from 'immutable';

const options = {
  data:new Immutable.List(),
  err : null
}

let defaultState = new Immutable.Map(options)

export default function dashboardReducer(state = defaultState, action){
  let clientCollection = state.get('data');
  switch (action.type) {
    case 'GET_CLIENT':
      return state
    case 'GET_CLIENT_SUCCESS':
      let clientNewData = new Immutable.List(action.res.data);
      return state.set('data', clientNewData);
    case 'GET_CLIENT_FAILURE':
      return state.set('err', action.err);
    case 'CREATE_CLIENT_SUCCESS':
      let addClientData = clientCollection.push(action.res.data);
      return state.set('data', addClientData);
    case 'CREATE_CLIENT_FAILURE':
      return state.set('ERR', action.err);
    case 'DELETE_CLIENT_SUCCESS':
      let idx = clientCollection.findIndex((client) => {
                return client.clientId === action.res;
            })
      clientCollection = clientCollection.delete(idx);
      return state.set('data', clientCollection);
    case 'DELETE_CLIENT_FAILURE':
      return state.set('err', action.err);
    default:
      return state
  }
}
