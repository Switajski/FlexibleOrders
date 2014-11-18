Ext.define('MyApp.model.KundeData', {
			extend : 'Ext.data.Model',

			fields : [{
						name : 'customerNumber'
					}, {
						name : 'companyName'
					}, {
						name : 'firstName'
					}, {
						name : 'lastName'
					}, {
						name : 'email'
					}, {
						name : 'phone'
					}, {
						name : 'name1'
					}, {
						name : 'name2'
					}, {
						name : 'street'
					}, {
						name : 'postalCode'
					}, {
						name : 'city'
					}, {
						name : 'country'
					},
					// TODO: make address a child data structure
					{
						name : 'dname1'
					}, {
						name : 'dname2'
					}, {
						name : 'dstreet'
					}, {
						name : 'dpostalCode'
					}, {
						name : 'dcity'
					}, {
						name : 'dcountry'
					}, // TODO: make customer details a child data structure
					{
						name : 'paymentConditions'
					}, {
						name : 'vatIdNo'
					}, {
						name : 'vendorNumber'
					}, {
						name : 'saleRepresentative'
					}, {
						name : 'mark'
					}, {
						name : 'contact1'
					}, {
						name : 'contact2'
					}, {
						name : 'contact3'
					},{
						name : 'notes'
					},{
						name : 'phone'
					},{
						name : 'fax'
					}]
		});