import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import * as actions from '../actions/clientActions';
import CreateClientForm from './clientForm';


class CreateClient extends Component {

    constructor(props,context) {
        super(props,context);
        this.state = {
            name: '', addressLine1: '', addressLine2: '', state: '', country: ''
        }
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    static contextTypes = {
        router: React.PropTypes.object.isRequired
    };

   handleSubmit(data) {
       this.props.createClientApi(data).then(promise =>{
           if(promise.type == "CREATE_CLIENT_SUCCESS"){
                this.context.router.push('/clients');
           }
       })
       
    }

    componentDidMount(){
        console.log(this.props)
    }

    render() {
        return (
            <div className="wrapper wrapper-content animated fadeInRight">
                <div className="row">
                    <div className="col-lg-12">
                        <div className="text-center m-t-lg">
                                <CreateClientForm data={this.props.data} handleSubmit={this.handleSubmit} />
                        </div>
                    </div>
                </div>
            </div>
        )
    }

}




@connect(state => ({data : state.client}))
export default class CreateClientContainer extends Component {
    static propTypes = {
      data: PropTypes.object,
      dispatch: PropTypes.func.isRequired
    };

    render() {
      const { data, dispatch} = this.props;
      console.log("====data....",this.props.data)
      return <CreateClient data={data} {...bindActionCreators({...actions},dispatch)} />;
    }
  }