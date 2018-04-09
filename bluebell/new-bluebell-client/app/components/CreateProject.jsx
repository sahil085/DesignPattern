import React, {Component} from 'react';
import Employee from '../views/Employee'
import EmployeeDetail from '../views/EmployeeDetail'
import StepZilla from 'react-stepzilla'

export default class CreateProject extends Component {

    render() {
        const steps =
            [
                {name: 'tep 1', component: <Employee />},
                {name: 'Step 2', component: <EmployeeDetail />},
            ];
        return (
            <div className="ibox">
                <div className="ibox-title create-project-heading">
                    <h1>Create Project</h1>
                    <div className="ibox-content">
                        <div className='step-progress'>
                            <StepZilla steps={steps} showNavigation={false}/>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}