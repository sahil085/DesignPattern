import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ItemList from './common/custom/ItemList';

export default class ClientList extends Component {

    constructor(props) {
        super(props);
        this.getTabledata = this.getTabledata.bind(this);
    }

    handleEdit = (data)=>{
        console.log('edit data is : ' + JSON.stringify(data));
    }

    handleDelete = (clientId)=>{
        this.props.deleteClient(clientId).then(promise=>{
            if(promise.type == 'DELETE_CLIENT_SUCCESS'){
                console.log('Client to be deleted is ' + clientId);
            }
        })
        
    }

    getTabledata(data){
        var dataSet = [];
        if(data != undefined){
            data.map((clients)=>{
                if(clients){
                    clients.map(value=>{
                        var newData = {};
                        newData.clientId = value.clientId;
                        //newData.active = value.active;
                        newData.clientName = value.clientName;
                        value.address ? newData.address = value.address.addressLine1 + value.address.addressLine2  : newData.address = '' ;
                        newData.projects = 'abc';
                        value.address ? value.address.state ? newData.state = value.address.state  : newData.state = 'abc' :  newData.state = 'xyz';
                        value.address ? value.address.country ? newData.country = value.address.country : newData.country = 'abc' : newData.country = 'xyz';
                        newData.actions = '';
                        dataSet.push(newData);
                    })
                }
            })
        }
        return dataSet;
    }

    render() {
        let columnMeta = [
            {
                "data": "clientName",
                "sClass" : "column-1"
            }, {
                "data": 'address',
            }, {
                "data": 'projects'
            }, {
                "data": 'state',
            },{
                "data": "country",
            },{
                "data" : "actions"
            }

        ];

        let heading = ["Client Name", "Address", "Project", "State", "Country","Actions"];
        let clientData =  this.getTabledata(this.props.data);
        const actionButtons = `<div class="dropdown"><span class="fa fa-ellipsis-v dropdown-toggle action_ellipsis_button" data-toggle="dropdown"></span>
                                <ul class="dropdown-menu action_buttons">
                                    <li><a id="editButton">Edit</a></li>
                                    <li><a id="deleteButton">Delete</a></li>
                                </ul>
                            </div>`;
        var editId = '#editButton';
        var deleteId = '#deleteButton';

        return (
                    <div className="col-lg-12">
                        <div className="ibox float-e-margins">
                            <div className="ibox-title"  style={{padding: 0}}>
                                <div className="col-lg-1" style={styles.titleHeading}>
                                    <h3 style={{color:'black'}}><strong>Client</strong></h3>
                                </div>
                                <div className="col-lg-10" style={styles.titleToolbar}>
                                    <span style={{display:'inline-block'}}><h4>Total Records : </h4></span><span style={styles.totalCount}>{clientData.length}</span>
                                </div>
                            </div>    
                            <div className="ibox-content">
                                {clientData.length > 0 ? <ItemList showActionButtons={true} editId={editId} deleteId={deleteId} actionButtons={actionButtons} handleEdit={this.handleEdit} handleDelete={this.handleDelete} data={clientData} heading={heading} columnMetadata={columnMeta}/> : ''} 
                            </div>
                        </div>
                    </div>
                )
    }
}

const styles = {

    titleHeading : {
        display: 'inline-block',
        borderRight: '2px solid #ddd',
        padding: '10px 10px 6px 15px'
    },
    titleToolbar : {
        display: 'inline-block',
        padding: '10px 10px 6px 15px'
    },
    totalCount:{
        color: 'blue',
        display: 'inline-block',
        fontWeight: '900',
        fontSize: 'medium',
        paddingLeft: '5px'
    }

}