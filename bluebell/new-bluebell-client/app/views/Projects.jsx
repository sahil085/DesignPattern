import React, {Component} from 'react';
import CreateProject from '../components/CreateProject';

export default class Project extends Component {

    render() {
        return (
            <div className="wrapper wrapper-content animated fadeInRight">
                <div className="row">
                    <CreateProject />
                </div>
            </div>
        )
    }

}
