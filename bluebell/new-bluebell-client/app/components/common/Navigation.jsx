import React, { Component } from 'react';
import { Dropdown } from 'react-bootstrap';
import { Link, Location } from 'react-router';

class Navigation extends Component {

    componentDidMount() {
        const { menu } = this.refs;
        $(menu).metisMenu();
    }

    activeRoute(routeName) {
        return this.props.location.pathname.indexOf(routeName) > -1 ? "active" : "";
    }

    secondLevelActive(routeName) {
        return this.props.location.pathname.indexOf(routeName) > -1 ? "nav nav-second-level collapse in" : "nav nav-second-level collapse";
    }

    render() {
        return (
            <nav className="navbar-default navbar-static-side" role="navigation">
                    <ul className="nav metismenu" id="side-menu" ref="menu">
                        <li className="nav-header">
                            <div className="dropdown profile-element"> <span>
                             </span>
                                <a data-toggle="dropdown" className="dropdown-toggle" href="#">
                            <span className="clear"> <span className="block m-t-xs"> <strong className="font-bold">Example user</strong>
                             </span> <span className="text-muted text-xs block">Example position<b className="caret"></b></span> </span> </a>
                                <ul className="dropdown-menu animated fadeInRight m-t-xs">
                                    <li><a href="#"> Logout</a></li>
                                </ul>
                            </div>
                            <div className="logo-element">
                                IN+
                            </div>
                        </li>
                        <li className={this.activeRoute("/dashboard")}>
                            <Link to="/dashboard"><i className="fa fa-th-large"></i> <span className="nav-label">Dashboards</span></Link>
                        </li>
                        <li className={this.activeRoute("/project")}>
                            <Link to="/project"><i className="fa fa-th-large"></i> <span className="nav-label">Projects</span></Link>
                        </li>
                        <li className={this.activeRoute("/clients")}>
                            <Link to="/clients"><i className="fa fa-th-large"></i> <span className="nav-label">Clients</span><span className="fa arrow"></span></Link>
                            <ul className={this.secondLevelActive("/clients")}>
                                <li>
                                    <Link to="/clients"><span className="nav-label">Client Details</span></Link>
                                </li>
                                <li>
                                    <Link to="/clients/createClient"><span className="nav-label">Create Client</span></Link>
                                </li>
                            </ul>
                        </li>
                        <li className={this.activeRoute("/employee")}>
                            <Link to="/employee"><i className="fa fa-th-large"></i> <span className="nav-label">Employee</span></Link>
                        </li>
                        <li className={this.activeRoute("/openneeds")}>
                            <Link to="/openneeds"><i className="fa fa-th-large"></i> <span className="nav-label">Open Needs</span></Link>
                        </li>
                        <li className={this.activeRoute("/bench")}>
                            <Link to="/bench"><i className="fa fa-th-large"></i> <span className="nav-label">Bench</span></Link>
                        </li>
                    </ul>

            </nav>
        )
    }
}

export default Navigation