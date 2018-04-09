import axios from 'axios';

const uri ='http://bluebell.qa2.intelligrape.net/bluebell';
const token = 'bb7e4b1e-5712-11e7-907b-a6006ad3dba0'

export function getClientApi() {
    return{
        types: ['GET_CLIENT' , 'GET_CLIENT_SUCCESS', 'GET_CLIENT_FAILURE'],
        promise : axios.get(uri + '/client',{
                        headers:{
                            'X-ACCESS-TOKEN' : token
                        }
                    }).then((data)=> {
                        return data
                    }).catch((err)=>{
                        console.log('error occured' + err.message);
                        return err
                    })
    }
}

export function deleteClientApi(clientId) {
    return{
        types: ['DELETE_CLIENT' , 'DELETE_CLIENT_SUCCESS', 'DELETE_CLIENT_FAILURE'],
        promise : axios.delete(uri + '/client/' + clientId,{
                        headers:{
                            'X-ACCESS-TOKEN' : token
                        }
                    }).then((data)=> {
                        return clientId
                    }).catch((err)=>{
                        console.log('error occured' + err.message);
                        return err
                    })
    }
}


export function createClientApi(clientForm) {
    return {
        types: ['CREATE_CLIENT', 'CREATE_CLIENT_SUCCESS', 'CREATE_CLIENT_FAILURE'],
        promise: axios.post(uri + '/client', clientForm, 
            { 
                headers: {
                    'X-ACCESS-TOKEN': token
                }
            }
        ).then((data) => {
            return data
        }).catch((err) => {
             console.log('error occured' + err.message);
            return err.message
        })
    }
}
