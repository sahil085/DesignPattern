import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import * as actions from '../actions/dashboardActions';
import DashBoardList from '../components/dashboardList';

class DashBoard extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount(){
        this.props.getRegionComBilableApi();
        this.props.getCompetenciesApi();
    }

    render() {
        return (
            <div className="wrapper wrapper-content animated fadeInRight">
                <div className="row">
                    <div>
                        <DashBoardList regionCompBilableData={this.props.regionCompBilableData} competenciesData={this.props.competenciesData}/>
                    </div>
                </div>
            </div>
        )
    }

}


@connect(state => ({regionCompBilableData : state.dashboard.get("regionCompBilableData"), competenciesData: state.dashboard.get("competenciesData")}))
export default class DashboardContainer extends Component {
    static propTypes = {
      regionCompBilableData: PropTypes.object,
      competenciesData: PropTypes.object,
      dispatch: PropTypes.func.isRequired
    };

    render() {
      const { regionCompBilableData, competenciesData, dispatch} = this.props;
      return <DashBoard regionCompBilableData={regionCompBilableData} competenciesData={competenciesData} {...bindActionCreators({...actions},dispatch)} />;
    }
  }