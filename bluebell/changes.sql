--17 Feb : Add Shadow allocation --
update PROJECT_STAFFING_REQUEST SET BILL_TYPE = 'NON_BILLABLE' where BILLABLE = 0;
update PROJECT_STAFFING_REQUEST SET BILL_TYPE = 'BILLABLE' where BILLABLE = 1;
alter table PROJECT_STAFFING_REQUEST modify BILLABLE bit(1) null;