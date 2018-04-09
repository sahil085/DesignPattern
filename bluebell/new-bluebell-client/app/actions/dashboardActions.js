import axios from 'axios';

const uri ='http://bluebell.qa2.intelligrape.net/bluebell/';
const token = 'bb7e4b1e-5712-11e7-907b-a6006ad3dba0'

export function getCompetenciesSuccess(data) {
    return{
        type: 'COMPETENCIES_SUCCESS',
        competencies : data
    }
}

export function getCompetenciesError(err) {
    return{
        type: 'COMPETENCIES_FAILURE',
        err : err
    }
}

export function getRegionCompBilableSuccess(data) {
    return{
        type: 'REGIONCOMPBILABLE_SUCCESS',
        regionCompBilable : data
    }
}

export function getRegionCompBilableError(err) {
    return{
        type: 'REGIONCOMPBILABLE_FAILURE',
        err : err
    }
}

export function getCompetenciesApi(){
    return (dispatch) => {

        axios.get(uri + '/competencies',{
            headers:{
                'X-ACCESS-TOKEN' : token
            }
        }).then((data)=> {
                dispatch(getCompetenciesSuccess(data));
            }).catch((err)=>{
                console.log('error occured' + err.message);
                dispatch(getCompetenciesError(err.message))    
            })
    }
}

export function getRegionComBilableApi(){
    return (dispatch) => {

        axios.post(uri + '/reports/region-competency-billable',
            {
                graphType:"Allocations",                
                projectType:"Billable",
                regions:[]
            },{
            headers:{
                'X-ACCESS-TOKEN' : token
            }
        }).then((data)=> {
                dispatch(getRegionCompBilableSuccess(data));
            }).catch((err)=>{
                console.log('error occured' + err.message);
                dispatch(getRegionCompBilableError(err.message))    
            })
    }
}