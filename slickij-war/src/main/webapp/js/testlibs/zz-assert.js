(function() {
	jShould.prototype.shouldHaveProperty = function(name, value) {
		if(typeof value != 'undefined') {
			ok(_.has(this.context, name) && this.context[name] === value, "The argument should have property '" + name + "' set to '" + value + "'.");
		} else {
			ok(_.has(this.context, name), "The argument should have property with name '" + name + "'.");
		}
		return this;
	}
	jShould.prototype.shouldBeInstanceOf = function(type, typeName) {
		if(_.isUndefined(typeName)) {
			ok(this.context instanceof type, "The argument should be an instance of the provided type.");
		} else {
			ok(this.context instanceof type, "The argument should have been of type '" + typeName + "'.");
		}
		return this;
	}
})();

Assert = $$.noConflict();
