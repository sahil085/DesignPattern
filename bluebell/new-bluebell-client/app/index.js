import React from 'react';
import {render} from 'react-dom';
import { Provider } from 'react-redux'
import { Router, hashHistory } from 'react-router';
import routes from './config/routes';

import configureStore from './store/configureStore'
import jquery from 'jquery'
import metismenu from 'metismenu'
import bootstrap from 'bootstrap'
import dataTable from 'datatables.net'
import slimscroll from 'jquery-slimscroll'

import './../node_modules/bootstrap/dist/css/bootstrap.min.css'
import './../node_modules/font-awesome/css/font-awesome.css'
import './../node_modules/animate.css/animate.min.css'
import './../public/styles/style.css'
import './../public/styles/datatables.min.css'
import './../public/vendor/dataTable/inspinia.js'
import './../public/vendor/pace/pace.js'

const store = configureStore()

render(
    <Provider store={store}>
        <Router history={hashHistory} routes={routes}/>
    </Provider>,
    document.getElementById('root')
);