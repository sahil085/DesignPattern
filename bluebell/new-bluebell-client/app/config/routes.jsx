import React from 'react'
import Main from '../components/layouts/Main';
import Blank from '../components/layouts/Blank';

import DashboardView from '../views/DashBoard';
import ProjectView from '../views/Projects';
import EmployeeView from '../views/Employee';
import ClientView from '../views/Client';
// import CreateClient from '../views/client/CreateClient';
import OpenNeedsView from '../views/OpenNeeds';
import BenchView from '../views/Bench';
import CreateClient from '../components/createClient';

import { Route, Router, IndexRedirect, browserHistory, IndexRoute} from 'react-router';

export default (
    <Router history={browserHistory}>
        <Route path="/" component={Main}>
            <IndexRedirect to="/dashboard" />
            <Route path="dashboard" component={DashboardView}> </Route>
            <Route path="project" component={ProjectView}> </Route>
            <Route path="clients" >
                <IndexRoute component={ClientView} />
                <Route path="createClient" component={CreateClient}></Route>   
            </Route>
            <Route path="employee" component={EmployeeView}> </Route>
            <Route path="openneeds" component={OpenNeedsView}> </Route>
            <Route path="bench" component={BenchView}> </Route>
        </Route>
    </Router>

);