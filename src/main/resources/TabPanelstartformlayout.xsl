<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:th="http://www.thymeleaf.org" exclude-result-prefixes="th"
	version="1.0">
	<xsl:output method="xhtml" omit-xml-declaration="yes" />


	<xsl:template match="/">
		<html>
			<xsl:attribute name="th:fragment">activitiForm</xsl:attribute>
			<body>
				<form method="POST" action="/app/secure/startprocess" id="submitForm">
					<xsl:attribute name="th:object">${taskform}</xsl:attribute>

					<input type="hidden" id="taskId">
						<xsl:attribute name="th:field"><xsl:text>*{datamap[__processDefinitionId__]}</xsl:text></xsl:attribute>

					</input>
					<input type="hidden" id="actiontag">
						<xsl:attribute name="th:field"><xsl:text>*{datamap[__actiontag__]}</xsl:text></xsl:attribute>
					</input>
					<ul class="nav nav-tabs" role="tablist">

						<xsl:for-each select="//layout">

							<li>
								<xsl:if test="(position( )) = 1">
									<xsl:attribute name="class">active</xsl:attribute>
								</xsl:if>
								<a role="tab" data-toggle="tab">
									<xsl:attribute name="href">#<xsl:value-of
										select="htmlID" /></xsl:attribute>

									<xsl:value-of select="label"></xsl:value-of>
								</a>
							</li>
						</xsl:for-each>
					</ul>
					<div class="tab-content">
						<xsl:apply-templates></xsl:apply-templates>
					</div>
					<div>
						<input type="submit" class="btn btn-primary" value="Start Process"
							onclick="javascript:submitForm(this)"></input>
						
					</div>
				</form>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="layout[not(preceding::layout or ancestor::layout)]">
		<div class="tab-pane fade active in">
			<xsl:attribute name="id">
    <xsl:value-of select="htmlID" />
  </xsl:attribute>
			<xsl:apply-templates select="columns/array"></xsl:apply-templates>
		</div>
	</xsl:template>
	<xsl:template match="layout">


		<div class="tab-pane fade">
			<xsl:attribute name="id">
    <xsl:value-of select="htmlID" />
  </xsl:attribute>
			<xsl:apply-templates select="columns/array"></xsl:apply-templates>
		</div>

	</xsl:template>
	<xsl:template match="columns/array">
		<xsl:if test="type/text()='modalsection'">
			<div  class="modal fade" role="dialog">
				<xsl:attribute name="id"><xsl:value-of
						select="htmlID" /></xsl:attribute>
				<div class="modal-dialog">

					<div class="modal-content">
						<div class="modal-body">
							<xsl:apply-templates select="columns/array"></xsl:apply-templates>
						</div>

						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>

			</div>
		</xsl:if>

		<xsl:if test="type/text()='modallink'">
			<a data-toggle="modal">
				<xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of
						select="htmlID" /></xsl:attribute>
				<xsl:value-of select="label"></xsl:value-of>
			</a>

		</xsl:if>
		<xsl:if test="type/text()='textbox'">


			<div>

				<xsl:attribute name="id"><xsl:text>div</xsl:text><xsl:value-of
						select="htmlID" /></xsl:attribute>
				<xsl:attribute name="class"><xsl:text>form-group</xsl:text>


					<xsl:if test="mandatory/text()='true'">

						<xsl:text> required </xsl:text>
					</xsl:if>

					<xsl:if test="hideData/text()='true'">

						<xsl:text> hidden </xsl:text>
					</xsl:if>



				</xsl:attribute>
				<label class='control-label' for="text">
					<xsl:value-of select="label"></xsl:value-of>
				</label>
				<input type="text" class="form-control">

					<xsl:attribute name="id">
						<xsl:value-of select="htmlID" />
					</xsl:attribute>
					<xsl:attribute name="th:field"><xsl:text>*{datamap[__</xsl:text><xsl:value-of
							select="htmlID" /><xsl:text>__]}</xsl:text></xsl:attribute>


					<xsl:choose>

						<xsl:when test="mandatory/text()='true'">

							<xsl:attribute name="required">required</xsl:attribute>
						</xsl:when>

						<xsl:when test="readonly/text()='true'">

							<xsl:attribute name="readonly">true</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>

						</xsl:otherwise>
					</xsl:choose>
				</input>
			</div>

		</xsl:if>
		<xsl:if test="type/text()='dropdown'">


			<div>
				<xsl:attribute name="class"><xsl:text>form-group</xsl:text>


					<xsl:if test="mandatory/text()='true'">

						<xsl:text> required </xsl:text>
					</xsl:if>

					<xsl:if test="hideData/text()='true'">

						<xsl:text> hidden </xsl:text>
					</xsl:if>



				</xsl:attribute>
				<label class='control-label' for="sel1">Select list:</label>
				<select class="form-control">
					<xsl:if test="mandatory/text()='true'">

						<xsl:attribute name="required">required</xsl:attribute>
					</xsl:if>
					<xsl:if test="readonly/text()='true'">

						<xsl:attribute name="readonly">true</xsl:attribute>
					</xsl:if>

					<xsl:if test="script/text() != ''">

						<xsl:attribute name="onchange"><xsl:value-of select="script"></xsl:value-of>
						</xsl:attribute>
					</xsl:if>
					<xsl:attribute name="id">
						<xsl:value-of select="htmlID" />
					</xsl:attribute>
					<xsl:attribute name="th:field"><xsl:text>*{datamap[__</xsl:text><xsl:value-of
							select="htmlID" /><xsl:text>__]}</xsl:text></xsl:attribute>
					<xsl:for-each select="options">
						<option>
							<xsl:attribute name="value">
								<xsl:value-of select="value" />
							</xsl:attribute>
							<xsl:value-of select="display"></xsl:value-of>
						</option>
					</xsl:for-each>





				</select>
			</div>

		</xsl:if>


		<xsl:if test="type/text()='radiobutton'">


			<div>

				<xsl:attribute name="id"><xsl:text>div</xsl:text><xsl:value-of
						select="htmlID" /></xsl:attribute>
				<xsl:attribute name="class"><xsl:text>form-group</xsl:text>


					<xsl:if test="mandatory/text()='true'">

						<xsl:text> required </xsl:text>
					</xsl:if>

					<xsl:if test="hideData/text()='true'">

						<xsl:text> hidden </xsl:text>
					</xsl:if>



				</xsl:attribute>

				<legend>	<xsl:value-of select="label"></xsl:value-of></legend>

				<xsl:for-each select="options">

					<div class="form-check">
						<label class="form-check-label">
							<input type="radio" class="form-check-input" >

								<xsl:attribute name="id">
									<xsl:value-of select="../htmlID" />
								</xsl:attribute>
								<xsl:attribute name="th:field"><xsl:text>*{datamap[__</xsl:text><xsl:value-of
										select="../htmlID" /><xsl:text>__]}</xsl:text></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="value" /></xsl:attribute>

							</input>
							<xsl:value-of select="display" />
						</label>
					</div>
				</xsl:for-each>



			</div>

		</xsl:if>
		<xsl:if test="type/text()='datepicker'">

			<div>

				<xsl:attribute name="id"><xsl:text>div</xsl:text><xsl:value-of
						select="htmlID" /></xsl:attribute>
				<xsl:attribute name="class"><xsl:text>form-group</xsl:text>


					<xsl:if test="mandatory/text()='true'">

						<xsl:text> required </xsl:text>
					</xsl:if>

					<xsl:if test="hideData/text()='true'">

						<xsl:text> hidden </xsl:text>
					</xsl:if>



				</xsl:attribute>
				<label class='control-label' for="text">
					<xsl:value-of select="label"></xsl:value-of>
				</label>
				<div class='input-group date' id='datetimepicker1'>
					<input type='text' class="form-control">

						<xsl:attribute name="id">
							<xsl:value-of select="htmlID" />
						</xsl:attribute>
						<xsl:attribute name="th:field"><xsl:text>*{datamap[__</xsl:text><xsl:value-of
								select="htmlID" /><xsl:text>__]}</xsl:text></xsl:attribute>
						<xsl:choose>

							<xsl:when test="mandatory/text()='true'">

								<xsl:attribute name="required">required</xsl:attribute>
							</xsl:when>

							<xsl:when test="readonly/text()='true'">

								<xsl:attribute name="readonly">true</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>

							</xsl:otherwise>
						</xsl:choose>
					</input>
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>