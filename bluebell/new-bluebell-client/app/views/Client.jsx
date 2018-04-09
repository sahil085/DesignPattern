import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import * as actions from '../actions/clientActions';
import ClientList from '../components/clientList';


class Client extends Component {

    constructor(props){
        super(props);
    }

    componentDidMount(){
        this.props.getClientApi().then(promise =>{
            if(promise.type == "GET_CLIENT_SUCCESS" ){
                console.log('success');
            }
        });
    }

    render() {
        console.log("inside render client");
        return (
            <div className="wrapper wrapper-content animated fadeInRight">
                <div className="row">
                     {this.props.children || <ClientList data={this.props.data} deleteClient={this.props.deleteClientApi}/>}
                </div>
            </div>
        )
    }

}

@connect(state => ({data : state.client}))
export default class ClientContainer extends Component {
    static propTypes = {
      data: PropTypes.object,
      dispatch: PropTypes.func.isRequired
    };

    render() {
      const { data, dispatch} = this.props;
      return <Client data={data} {...bindActionCreators({...actions},dispatch)} />;
    }
  }
