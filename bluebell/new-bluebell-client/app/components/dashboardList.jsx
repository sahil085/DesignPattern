import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ItemList from './common/custom/ItemList';

export default class DashBoardList extends Component {

    constructor(props) {
        super(props);
        this.getTabledata = this.getTabledata.bind(this);
    }

    getTabledata(data){

        if(data == undefined){
            return
        }else{
            var dataSet = [];
            data.map((value)=>{
                var comp = [];
                this.props.competenciesData.map(competency => {
                    var regionList = value.competency;
                })
            })
            return dataSet;
        }
    }

    render() {
        let columnMeta = [
            {
                "data": "competency"
            }, {
                "data": 'Middle East and Africa',
            }, {
                "data": 'Global Innovation Hub'
            }, {
                "data": 'South East Asia',
            },{
                "data": "US Central",
            },{
                "data": "Corporate Marketing",
            },{
                "data": "Admin India",
            },{
                "data": "European Union",
            },{
                "data": "US East",
            },{
                "data": "Video Managed Services",
            },{
                "data": "Managed Services",
            },{
                "data": "Digital Marketing",
            },{
                "data": "Australia-New Zealand ",
            },{
                "data": "Global",
            },{
                "data": "US West",
            },{
                "data": "India",
            },{
                "data": "IT India",
            },{
                "data": "Finance SEA",
            },{
                "data": "Global Sales",
            },{
                "data": "HDFC",
            },{
                "data": "Video Ready",
            },{
                "data": "SmartTV",
            },{
                "data": "Global Human Resources",
            },{
                "data": "Finance India",
            }
        ];
        
        var regionCompBilableData = this.getTabledata(this.props.regionCompBilableData);

        return (
            <div className="wrapper wrapper-content animated fadeInRight">
                <div className="row">
                    <div className="col-lg-12">
                    </div>
                </div>
            </div>
        )
    }

}
