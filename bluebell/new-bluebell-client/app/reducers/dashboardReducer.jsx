import Immutable from 'immutable';

const options = {
  competenciesData : new Immutable.List(),
  regionCompBilableData : new Immutable.List(),
  err : null
}

let defaultState = new Immutable.Map(options)

export default function dashboardReducer(state = defaultState, action){
  let competenciesCollection = state.get('competenciesData');
  let regionCompBilableCollection = state.get('regionCompBilableData');
  switch (action.type) {
    case 'COMPETENCIES_SUCCESS':
      let competenciesNewData = new Immutable.List(action.competencies.data);
      return state.set('competenciesData', competenciesNewData);
    case 'COMPETENCIES_FAILURE':
      return state.set('err', action.err);
    case 'REGIONCOMPBILABLE_SUCCESS':
      var arr = [];
      arr.push(action.regionCompBilable.data)
      let regionCompBilableNewData = new Immutable.List(arr);
      return state.set('regionCompBilableData', regionCompBilableNewData);
    case 'REGIONCOMPBILABLE_FAILURE':
      return state.set('err', action.err);
    default:
      return state
  }
}
