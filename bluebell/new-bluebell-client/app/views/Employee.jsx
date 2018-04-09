import React, {Component} from 'react';
import profile1 from "../../public/img/profile.jpg"

import EmployeeDetail from "./EmployeeDetail"

export default class Employee extends Component {

    render() {
        const ulStyle = {
            height: 0
        };
        $('.competency').click(function () {
            $('.competency').addClass('active');
            $('.ibox-content').css("display", "none");
        })
        return (
            <div>
                <div className="wrapper wrapper-content animated fadeInRight">
                    <div className="wrapper wrapper-content animated fadeInRight main-wrapper">
                        <div className="row employee">
                            <div className="col-md-12">
                                <div className="ibox float-e-margins">
                                    <div className="ibox-title">
                                        <h2>Employee Search</h2>
                                        <span className="searchbox-employee">
                                        <input type="text" className="search-box search-box-employee"
                                               placeholder="Search by Name,Competency"/>
                                        <span className="fa fa-search search-icon-employee"></span>
                                        </span>
                                        <hr />
                                        <div className="row main-section">
                                            <div className="col-md-3">
                                                <div
                                                    className="wrapper wrapper-content animated fadeInRight accordians">
                                                    <div
                                                        className="wrapper wrapper-content animated fadeInRight accordian">
                                                        <div className="row">
                                                            <div className="col-md-12 accordian-border">
                                                                <div className="ibox margin-between-accordians">
                                                                    <a className="collapse-link">
                                                                        <div className="ibox-title accordian-title">
                                                                            <div className="ibox-tools">
                                                                                <h5>Competency</h5>
                                                                                <i className="fa fa-chevron-up"></i>
                                                                            </div>
                                                                        </div>
                                                                    </a>
                                                                    <ul className="ibox-content list-of-contents">
                                                                        <li><input type="checkbox"
                                                                                   className="regular-checkbox big-checkbox"/>
                                                                            <label>AMC</label></li>
                                                                        <li><input type="checkbox"/> <label> Big
                                                                            Data</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label>
                                                                            Design</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> Mobile
                                                                            Windows</label></li>
                                                                        <li><input type="checkbox"/><label>
                                                                            Drupal</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> Testing
                                                                            Mobile</label></li>
                                                                        <li><input type="checkbox"/><label> JVM</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> MEAN</label>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                                <div className="ibox margin-between-accordians">
                                                                    <a className="collapse-link">
                                                                        <div className="ibox-title accordian-title">
                                                                            <div className="ibox-tools">
                                                                                <h5>Competency</h5>
                                                                                <i className="fa fa-chevron-up"></i>
                                                                            </div>
                                                                        </div>
                                                                    </a>
                                                                    <ul className="ibox-content list-of-contents">
                                                                        <li><input type="checkbox"
                                                                                   className="regular-checkbox big-checkbox"/>
                                                                            <label>AMC</label></li>
                                                                        <li><input type="checkbox"/> <label> Big
                                                                            Data</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label>
                                                                            Design</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> Mobile
                                                                            Windows</label></li>
                                                                        <li><input type="checkbox"/><label>
                                                                            Drupal</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> Testing
                                                                            Mobile</label></li>
                                                                        <li><input type="checkbox"/><label> JVM</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> MEAN</label>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                                <div className="ibox margin-between-accordians">
                                                                    <a className="collapse-link">
                                                                        <div className="ibox-title accordian-title">
                                                                            <div className="ibox-tools">
                                                                                <h5>Competency</h5>
                                                                                <i className="fa fa-chevron-up"></i>
                                                                            </div>
                                                                        </div>
                                                                    </a>
                                                                    <ul className="ibox-content list-of-contents">
                                                                        <li><input type="checkbox"
                                                                                   className="regular-checkbox big-checkbox"/>
                                                                            <label>AMC</label></li>
                                                                        <li><input type="checkbox"/> <label> Big
                                                                            Data</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label>
                                                                            Design</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> Mobile
                                                                            Windows</label></li>
                                                                        <li><input type="checkbox"/><label>
                                                                            Drupal</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> Testing
                                                                            Mobile</label></li>
                                                                        <li><input type="checkbox"/><label> JVM</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> MEAN</label>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                                <div className="ibox margin-between-accordians">
                                                                    <a className="collapse-link">
                                                                        <div className="ibox-title accordian-title">
                                                                            <div className="ibox-tools">
                                                                                <h5>Competency</h5>
                                                                                <i className="fa fa-chevron-up"></i>
                                                                            </div>
                                                                        </div>
                                                                    </a>
                                                                    <ul className="ibox-content list-of-contents">
                                                                        <li><input type="checkbox"
                                                                                   className="regular-checkbox big-checkbox"/>
                                                                            <label>AMC</label></li>
                                                                        <li><input type="checkbox"/> <label> Big
                                                                            Data</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label>
                                                                            Design</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> Mobile
                                                                            Windows</label></li>
                                                                        <li><input type="checkbox"/><label>
                                                                            Drupal</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> Testing
                                                                            Mobile</label></li>
                                                                        <li><input type="checkbox"/><label> JVM</label>
                                                                        </li>
                                                                        <li><input type="checkbox"/><label> MEAN</label>
                                                                        </li>
                                                                    </ul>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div className="col-md-9">
                                                <h4>Showing 5 of 250 records</h4>
                                                <hr />
                                                <div className="row">
                                                    <div className="col-md-6 profile-details">
                                                        <div className="col-md-3"><img src={profile1}
                                                                                       alt="profile pic"/>
                                                        </div>
                                                        <div className="col-md-8 info-employee">
                                                            <h3>TANU SIWAG <span>Resigned</span></h3>
                                                            <h4>Senior Engineer - Technology</h4>
                                                            <p>Joining Date : <span>8 march, 2017</span></p>
                                                            <p>Primary Competency : <span>JVM</span></p>
                                                            <p>Email : <span>tanu.siwag@tothenew.com</span></p>
                                                        </div>
                                                        <p className="mentovisor">Mentovisor Name :-
                                                            <span>Himanshu Seth</span>
                                                        </p>
                                                        <p>Current Utilization</p>
                                                        <ul>
                                                            <li>Billable: <span>100%</span></li>
                                                            <li>Non-Billable: <span>0%</span></li>
                                                        </ul>
                                                    </div>
                                                    <div className="col-md-3 middle-section">
                                                        <h5>Previous Projects</h5>
                                                        <ul>
                                                            <li><a>Videoready <span>(80%)</span></a></li>
                                                            <li><a>TataSky <span>(40%)</span></a></li>
                                                            <li><a>Axis Bank <span>(20%)</span></a></li>
                                                            <li><a>HDFC <span>(60%)</span></a></li>
                                                        </ul>
                                                    </div>
                                                    <div className="col-md-3 last-section">
                                                        <h5>Current Projects</h5>
                                                        <ul>
                                                            <li><a>Simple Interact <span>(70%)</span></a></li>
                                                            <li><a>Westcon <span>(30%)</span></a></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                                <div className="row buttons">
                                                    <div className="col-md-6">
                                                        <button className="resign-button employee-buttons">Mark as
                                                            Resign
                                                        </button>
                                                    </div>
                                                    <div className="col-md-offset-2 col-md-4 right-side-buttons">
                                                        <button className="assign-button employee-buttons">Assign
                                                        </button>
                                                        <button className="mentovisor-button employee-buttons">Change
                                                            Mentovisor
                                                        </button>
                                                    </div>
                                                </div>
                                                <hr />
                                                <div className="row">
                                                    <div className="col-md-6 profile-details">
                                                        <div className="col-md-3"><img src={profile1}
                                                                                       alt="profile pic"/>
                                                        </div>
                                                        <div className="col-md-8 info-employee">
                                                            <h3>TANU SIWAG <span>Resigned</span></h3>
                                                            <h4>Senior Engineer - Technology</h4>
                                                            <p>Joining Date : <span>8 march, 2017</span></p>
                                                            <p>Primary Competency : <span>JVM</span></p>
                                                            <p>Email : <span>tanu.siwag@tothenew.com</span></p>
                                                        </div>
                                                        <p className="mentovisor">Mentovisor Name :-
                                                            <span>Himanshu Seth</span>
                                                        </p>
                                                        <p>Current Utilization</p>
                                                        <ul>
                                                            <li>Billable: <span>100%</span></li>
                                                            <li>Non-Billable: <span>0%</span></li>
                                                        </ul>
                                                    </div>
                                                    <div className="col-md-3 middle-section">
                                                        <h5>Previous Projects</h5>
                                                        <ul>
                                                            <li><a>Videoready <span>(80%)</span></a></li>
                                                            <li><a>TataSky <span>(40%)</span></a></li>
                                                            <li><a>Axis Bank <span>(20%)</span></a></li>
                                                            <li><a>HDFC <span>(60%)</span></a></li>
                                                        </ul>
                                                    </div>
                                                    <div className="col-md-3 last-section">
                                                        <h5>Current Projects</h5>
                                                        <ul>
                                                            <li><a>Simple Interact <span>(70%)</span></a></li>
                                                            <li><a>Westcon <span>(30%)</span></a></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                                <div className="row buttons">
                                                    <div className="col-md-6">
                                                        <button className="resign-button employee-buttons">Mark as
                                                            Resign
                                                        </button>
                                                    </div>
                                                    <div className="col-md-offset-2 col-md-4 right-side-buttons">
                                                        <button className="assign-button employee-buttons">Assign
                                                        </button>
                                                        <button className="mentovisor-button employee-buttons">Change
                                                            Mentovisor
                                                        </button>
                                                    </div>
                                                </div>
                                                <hr />
                                                <div className="row">
                                                    <div className="col-md-6 profile-details">
                                                        <div className="col-md-3"><img src={profile1}
                                                                                       alt="profile pic"/>
                                                        </div>
                                                        <div className="col-md-8 info-employee">
                                                            <h3>TANU SIWAG <span>Resigned</span></h3>
                                                            <h4>Senior Engineer - Technology</h4>
                                                            <p>Joining Date : <span>8 march, 2017</span></p>
                                                            <p>Primary Competency : <span>JVM</span></p>
                                                            <p>Email : <span>tanu.siwag@tothenew.com</span></p>
                                                        </div>
                                                        <p className="mentovisor">Mentovisor Name :-
                                                            <span>Himanshu Seth</span>
                                                        </p>
                                                        <p>Current Utilization</p>
                                                        <ul>
                                                            <li>Billable: <span>100%</span></li>
                                                            <li>Non-Billable: <span>0%</span></li>
                                                        </ul>
                                                    </div>
                                                    <div className="col-md-3 middle-section">
                                                        <h5>Previous Projects</h5>
                                                        <ul>
                                                            <li><a>Videoready <span>(80%)</span></a></li>
                                                            <li><a>TataSky <span>(40%)</span></a></li>
                                                            <li><a>Axis Bank <span>(20%)</span></a></li>
                                                            <li><a>HDFC <span>(60%)</span></a></li>
                                                        </ul>
                                                    </div>
                                                    <div className="col-md-3 last-section">
                                                        <h5>Current Projects</h5>
                                                        <ul>
                                                            <li><a>Simple Interact <span>(70%)</span></a></li>
                                                            <li><a>Westcon <span>(30%)</span></a></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                                <div className="row buttons">
                                                    <div className="col-md-6">
                                                        <button className="resign-button employee-buttons">Mark as
                                                            Resign
                                                        </button>
                                                    </div>
                                                    <div className="col-md-offset-2 col-md-4 right-side-buttons">
                                                        <button className="assign-button employee-buttons">Assign
                                                        </button>
                                                        <button className="mentovisor-button employee-buttons">Change
                                                            Mentovisor
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <EmployeeDetail/>
            </div>
        )
    }

};
