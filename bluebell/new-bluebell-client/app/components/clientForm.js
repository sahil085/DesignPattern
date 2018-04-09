import React, { Component } from 'react';
import Countries  from 'react-select-country';
 

const defaultState = 
{
        name: '',
        addressLine1: '',
        addressLine2: '',
        state: '',
        country: '',
        city:'',
        zipcode:''  
}

export default class CreateClientForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            ...defaultState              
        }
        this.handleChange = this.handleChange.bind(this);
        this.onSelectCountry=this.onSelectCountry.bind(this);
    }

    onSelectCountry(event){
        // this.state.country={
        //     id:event.target.value,
        //     name:event.target.options[event.target.selectedIndex].text
        // }
        //OR,if you assign "ref" to the component , , 
        this.state.country=this.refs.country.selected.name; // {value,name} 
    }

    handleChange(event) {

        this.setState(
            { [event.target.name]: event.target.value }
        );

    };

    handleSubmit = () => {
        var errors = this.validate();
        if (Object.keys(errors).length != 0) {
            this.setState({
                errors: errors
            });
            return;
        } else {
            this.props.handleSubmit(this.createRequestData({ ...this.state }));
            
        }

    }

    createRequestData = (formData) => {
        return {
            clientName: formData.name,
            clientId: 0,
            address: {
                addressLine1: formData.addressLine1,
                addressLine2: formData.addressLine2,
                city:formData.city,
                country: formData.country,
                state: formData.state,
                zipCode:formData.zipcode
            },
            active:true
        };
    }

    validate = () => {
        const errors = {};
        if (!this.state.name) {
            errors.name = 'Please enter a name';
        }

        if (!this.state.addressLine1 && !this.state.addressLine2) {
            errors.addressLine1 = 'Please enter address';
        }

        if (!this.state.state) {
            errors.state = 'Please enter a State';
        }

        if (!this.state.country) {
            errors.country = 'Please enter a country'
        }

        if (!this.state.city) {
            errors.city = 'Please enter a city'
        }

        if (!this.state.zipcode) {
             errors.zipcode = 'Please enter a zipcode'
        }    
            return errors;
    };


    render() {
        return (
            <div className="wrapper wrapper-content animated fadeInRight">
                <div className="row">
                    <div className="col-lg-12">
                        <div className="ibox float-e-margins">
                            <div className="ibox-title">
                                <h5>Create Client</h5>
                            </div>
                            <div className="ibox-content">

                                <form method="get" className="form-horizontal">
                                    <div className="form-group">
                                        <label className="col-sm-2 control-label">Name</label>

                                        <div className="col-sm-5">
                                            <input type="text" name="name" ref="name" value={this.props.name} onChange={this.handleChange} className="form-control" />
                                            {this.state.errors && this.state.errors.name && <span className="help-block m-b-none">{this.state.errors.name}</span>}
                                        </div>
                                    </div>
                                    <div className="form-group">
                                        <label className="col-sm-2 control-label">Address Line 1</label>

                                        <div className="col-sm-5">
                                            <input type="text" name="addressLine1" ref="addressLine1" value={this.props.addressLine1} className="form-control" onChange={this.handleChange} />
                                            {this.state.errors && this.state.addressLine1.error && <span className="help-block m-b-none">{this.state.addressLine1.error}</span>}
                                        </div>

                                    </div>
                                    <div className="form-group">
                                        <label className="col-sm-2 control-label">Address Line 2</label>

                                        <div className="col-sm-5">
                                            <input type="text" name="addressLine2" ref="addressLine2" value={this.props.addressLine2} className="form-control" onChange={this.handleChange} />
                                            {this.state.errors && this.state.addressLine2.error && <span className="help-block m-b-none">{this.state.addressLine2.error}</span>}
                                        </div>

                                    </div>
                                    <div className="form-group">
                                        <label className="col-sm-2 control-label">City</label>
                                        <div className="col-sm-5">
                                            <input type="text" className="form-control" name="city" value={this.props.state} ref="city" onChange={this.handleChange}/>
                                            {this.state.errors && this.state.errors.city && <span className="help-block m-b-none">{this.state.errors.city}</span>}
                                        </div>
                                    </div>
                                    <div className="form-group">
                                        <label className="col-sm-2 control-label">State</label>
                                        <div className="col-sm-5">
                                            <input type="text" className="form-control" name="state" value={this.props.state} ref="state" onChange={this.handleChange}/>
                                            {this.state.errors && this.state.errors.state && <span className="help-block m-b-none">{this.state.errors.state}</span>}
                                        </div>
                                    </div>
                                    
                                    <div className="form-group">
                                        <label className="col-sm-2 control-label">Country</label>
                                        <div className="col-sm-5">
                                                <Countries className="form-control m-b" ref="country" name="country" empty=" -- Select country --"  onChange={(e)=>this.onSelectCountry(e)} />
                                                {/*<select className="form-control m-b" name="country" value={this.props.country} ref="country" onChange={this.handleChange}>
                                                <option value="">Select Country</option>
                                                <option value="option2">option 2</option>
                                                <option value="option3">option 3</option>
                                                <option value="option4">option 4</option>
                                            </select>*/}
                                            {this.state.errors && this.state.errors.country && <span className="help-block m-b-none">{this.state.errors.country}</span>}
                                        </div>
                                    </div>
                                    <div className="form-group">
                                        <label className="col-sm-2 control-label">Zip Code</label>
                                        <div className="col-sm-5">
                                            <input type="number" className="form-control" name="zipcode" value={this.props.state} ref="zipcode" onChange={this.handleChange}/>
                                            {this.state.errors && this.state.errors.zipcode && <span className="help-block m-b-none">{this.state.errors.zipcode}</span>}
                                        </div>
                                    </div>
                                    <div className="form-group">
                                        <div className="col-sm-4 col-sm-offset-2">
                                            <button className="btn btn-primary" onClick={this.handleSubmit} type="button">Create</button>
                                        </div>
                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )


    }

}

