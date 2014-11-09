Ext
		.define(
				'MyApp.view.ConfirmWindow',
				{
					extend : 'MyApp.view.TransitionWindow',
					title : 'Auftrag best&auml;tigen',
					width : 500,
					id : 'ConfirmWindow',
					requires : [ 'MyApp.view.DetailsFieldset' ],
					bottomGrid : {
						xtype : 'PositionGrid',
						dock : 'bottom',
						id : Ext.getCmp('CreateConfirmationReportItemGrid'),
						flex : 1,
						store : 'CreateConfirmationReportItemDataStore',
						title : "Auftragsbest&auml;tigungspositionen",
						features : null,
						selType : 'cellmodel',
						plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
							clicksToEdit : 1
						}) ],
						features : [ {
							ftype : 'grouping',
							groupHeaderTpl : '{columnName}: {name} ({rows.length} Position{[values.rows.length > 1 ? "en" : ""]}) {[values.rows[0].created]}',
							hideGroupedHeader : false,
							startCollapsed : false
						// id: 'orderNumber'
						} ],
						columns : [
								{
									xtype : 'gridcolumn',
									dataIndex : 'product',
									text : 'Artikel',
									width : 85,
									displayField : 'name',
									valueField : 'productNumber'
								},
								{
									xtype : 'gridcolumn',
									dataIndex : 'productName',
									flex : 1,
									text : 'Artikel Name'
								},
								{
									xtype : 'gridcolumn',
									dataIndex : 'quantityLeft',
									width : 50,
									text : 'Menge'/*
													 * , editor : { xtype :
													 * 'numberfield', allowBlank :
													 * false, minValue : 1 }
													 */
								},
								{
									xtype : 'actioncolumn',
									width : 30,
									sortable : false,
									menuDisabled : true,
									items : [ {
										icon : '/FlexibleOrders/images/delete.png',
										tooltip : 'Position l&ouml;schen',
										scope : this,
										handler : function(grid, rowIndex) {
											Ext
													.getStore(
															'CreateConfirmationReportItemDataStore')
													.removeAt(rowIndex);
										}
									} ]
								} ]
					},
					headerForm : {
						xtype : 'fieldset',
						defaults : {},
						items : [
								{
									xtype : 'fieldcontainer',
									layout : 'hbox',
									defaults : {
										labelWidth : 70,
									},
									items : [ {
										xtype : 'displayfield',
										name : 'customerNumber',
										fieldLabel : 'Kundennr'
									}, {
										xtype : 'displayfield',
										name : 'firstName',
										margin : '0 0 0 10',
										labelWidth : 40,
										fieldLabel : 'Name'
									}, {
										xtype : 'displayfield',
										name : 'lastName',
										margin : '0 0 0 6',
										hideLabel : 'true'
									} ]
								},
								{
									xtype : 'fieldcontainer',
									layout : 'hbox',
									defaults : {
										labelWidth : 70,
										flex : 1,
										anchor : '100%'
									},
									items : [
											{
												id : 'newOrderConfirmationNumber',
												xtype : 'textfield',
												fieldLabel : 'ABnr',
												name : 'orderConfirmationNumber'
											}, {
												xtype : 'datefield',
												format : 'd/m/Y',
												allowBlank : true,
												fieldLabel : 'Liefertermin',
												minValue : Ext.Date.format(
														new Date(), 'd/m/Y'),
												minText : 'Datum liegt in der Vergangenheit',
												name : 'expectedDelivery',
												margin : '0 0 0 6'
											} ]
								}, {
									xtype : 'fieldcontainer',
									layout : 'hbox',
									defaults : {
										labelWidth : 70,
										anchor : '100%',
										flex : 1
									},
									items : [ {
										xtype : 'deliverymethodcombobox',
										store : 'DeliveryMethodDataStore',
										id : 'confirmDMComboBox',
										fieldLabel : 'Lieferart'
									} ]
								}, {
									xtype : 'detailsFieldset',
									title : 'Details'
								} ]

					},
					onSave : function(button, event, option) {
						MyApp.getApplication().getController(
								'ConfirmController').confirm(button, event,
								option);
					}
				});