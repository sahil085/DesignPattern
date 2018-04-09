import React from 'react';
import { connect } from 'react-redux';
import { initialize } from 'redux-form';
import CreateClientForm from './clien';
import * as actions from '../../actions/clientActions';
import { bindActionCreators } from 'redux';

@connect(state => ({data : state.dashboard}))
export default class CreateClient extends React.Component {
  constructor(props){
    super(props);
    this.handleSubmit=this.handleSubmit.bind(this);
  }


   handleSubmit(e) {
        e.preventDefaults();
        var errors = this.validate();
        if (Object.keys(errors).length != 0) {
            this.setState({
                errors: errors
            });
            return;
        }
        console.log('Submission received!', );
        // this.props.dispatch(initialize('client', {})); // clear form
    }



    validate() {
        const errors = {};

        if (!this.state.name) {
            errors.name = 'Please enter a name';
        }

        if (!this.state.addressLine1) {
            errors.addressLine1 = 'Please enter address';
        }

        if (!this.state.state) {
            errors.state = 'Please enter a State';
        }

        if (!this.state.country) {
            errors.country = 'Please enter a country'
        }

        return errors;
    };

  render() {
    return (
      <div id="app">
        <h1>App</h1>
        <CreateClientForm onSubmit={this.handleSubmit}/>
      </div>
    );
  }
}



