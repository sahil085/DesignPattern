import React, {Component} from 'react';
import profile1 from "../../public/img/profile.jpg"


export default class EmployeeDetail extends Component {
    render() {
        return (
            <div className="wrapper wrapper-content animated fadeInRight gf">
                <div className="wrapper wrapper-content animated fadeInRight">
                    <div className="col-md-12 container-temporary">
                        <div className="ibox float-e-margins">
                            <div className="ibox-title ibox-wrapper">
                                <div className="row">
                                    <div className="col-md-4 left-employee-detail">
                                        <div className="row image-small-employee">
                                            <div className="col-md-3 employee-image">
                                                <img src={profile1} alt="profile pic"/>
                                            </div>
                                            <div className="col-md-8 employee-info">
                                                <h3>TANU SIWAG</h3>
                                                <h4>Senior Engineer - Technology</h4>
                                                <p>tanu.siwag@tothenew.com</p>
                                            </div>
                                        </div>

                                        <div className="ibox margin-between-accordians">
                                            <a className="collapse-link">
                                                <div className="ibox-title accordian-title">
                                                    <div className="ibox-tools">
                                                        <h5>Official Information</h5>
                                                        <i className="fa fa-chevron-up"></i>
                                                    </div>
                                                </div>
                                            </a>
                                            <div className="ibox-content list-of-contents">
                                                <div className="row">
                                                    <div className="col-md-3">
                                                        <p>Legal Entity</p>
                                                        <p>Business Unit</p>
                                                        <p>Function</p>
                                                        <p>Region</p>
                                                        <p>Competency</p>
                                                        <p>Project</p>
                                                        <p>Mentovisor</p>
                                                    </div>

                                                    <div className="col-md-offset-2 col-md-7 employee-full-details">
                                                        <p>Intelligrape Software Pvt. Ltd</p>
                                                        <p>Technology</p>
                                                        <p>Delivery</p>
                                                        <p>India</p>
                                                        <p>JVM</p>
                                                            <ul>
                                                                <li>Westcon</li>
                                                                <li>Startup India</li>
                                                            </ul>
                                                        <p>Sandeep Bose</p>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="ibox margin-between-accordians">
                                                <a className="collapse-link">
                                                    <div className="ibox-title accordian-title">
                                                        <div className="ibox-tools">
                                                            <h5>Competencies</h5>
                                                            <i className="fa fa-chevron-up"></i>
                                                        </div>
                                                    </div>
                                                </a>
                                                <div className="ibox-content list-of-contents competencies">
                                                    <span>Java Script</span>
                                                    <span>JSP</span>
                                                    <span>Struts</span>
                                                    <span>Hibernate</span>
                                                    <span>Jquery</span>
                                                    <span>J2ee Application</span>
                                                    <span>JDBC</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-8 right-employee-detail-heading">
                                        <div className="employee-feedback">
                                            <h3>Feedback</h3>
                                            <h3 className="pull-right">Total Feedback: <span>4</span></h3>
                                        </div>
                                        <hr />

                                        <div className="employee-detail-container">
                                            <div className="row">
                                                <div className="col-md-1 feedback-person-image">
                                                    <img src={profile1} alt="profile pic"/>
                                                </div>
                                                <div className="col-md-5 feedback-person-detail">
                                                    <h4>Divyanshu Bhushan</h4>
                                                    <h4>Assistant Vice President - Technology</h4>
                                                </div>
                                                <div className="col-md-6 feedback-person-project">
                                                    <h4 className="pull-right">Project : <span>Family Education</span></h4>
                                                </div>
                                            </div>
                                            <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                                                Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
                                                when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                                                It has survived not only five centuries, but also the leap into electronic typesetting,
                                                remaining <a>Readmore...</a></p>
                                            <table className="table table-striped feedback-detail-table">
                                                <thead>
                                                <tr>
                                                    <th>Skill</th>
                                                    <th>Mentovisor Assessment</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>Meeting Timelines</td>
                                                    <td>Level 3: About half of the time</td>
                                                </tr>
                                                <tr>
                                                    <td>Quality / Finish of Deliverables</td>
                                                    <td>Level 3: Good - Reasonably acceptable quality is delivered most of the times</td></tr>
                                                <tr>
                                                    <td>Ownership</td>
                                                    <td>Level 3: Good - Demonstrates ownership most of the times
                                                        and takes responsibility for the end result</td>
                                                    </tr>
                                                <tr>
                                                    <td>Communication(Keeping all stakeholders informed of risks, progress, dependencies, etc)</td>
                                                    <td>Level 3: Fair: Selected information is sent to selected stakeholders.
                                                    Good stuff is communicated, but any delays are not communicated until the last minute.
                                                     Sometimes, external dependencies are not flagged in a timely manner.</td>
                                                </tr>
                                                <tr>
                                                    <td>Speed</td>
                                                    <td>Level 3: Good - Most of the tasks are completed on time without much follow-up.
                                                        There is a tendency to keep the ball moving</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <hr />

                                        <div className="employee-detail-container">
                                            <div className="row">
                                                <div className="col-md-1 feedback-person-image">
                                                    <img src={profile1} alt="profile pic"/>
                                                </div>
                                                <div className="col-md-5 feedback-person-detail">
                                                    <h4>Divyanshu Bhushan</h4>
                                                    <h4>Assistant Vice President - Technology</h4>
                                                </div>
                                                <div className="col-md-6 feedback-person-project">
                                                    <h4 className="pull-right">Project : <span>Family Education</span></h4>
                                                </div>
                                            </div>
                                            <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                                                Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
                                                when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                                                It has survived not only five centuries, but also the leap into electronic typesetting,
                                                remaining <a>Readmore...</a></p>
                                            <table className="table table-striped feedback-detail-table">
                                                <thead>
                                                <tr>
                                                    <th>Skill</th>
                                                    <th>Mentovisor Assessment</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>Meeting Timelines</td>
                                                    <td>Level 3: About half of the time</td>
                                                </tr>
                                                <tr>
                                                    <td>Quality / Finish of Deliverables</td>
                                                    <td>Level 3: Good - Reasonably acceptable quality is delivered most of the times</td></tr>
                                                <tr>
                                                    <td>Ownership</td>
                                                    <td>Level 3: Good - Demonstrates ownership most of the times
                                                        and takes responsibility for the end result</td>
                                                </tr>
                                                <tr>
                                                    <td>Communication(Keeping all stakeholders informed of risks, progress, dependencies, etc)</td>
                                                    <td>Level 3: Fair: Selected information is sent to selected stakeholders.
                                                        Good stuff is communicated, but any delays are not communicated until the last minute.
                                                        Sometimes, external dependencies are not flagged in a timely manner.</td>
                                                </tr>
                                                <tr>
                                                    <td>Speed</td>
                                                    <td>Level 3: Good - Most of the tasks are completed on time without much follow-up.
                                                        There is a tendency to keep the ball moving</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
};