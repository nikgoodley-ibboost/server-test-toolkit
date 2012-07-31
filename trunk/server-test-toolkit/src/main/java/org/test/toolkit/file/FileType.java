package org.test.toolkit.file;

public interface FileType {

	public String getExtension();

	public enum Image implements FileType {

		JPG;

		public String toString() {
			return "." + super.toString().toLowerCase();
		}

		@Override
		public String getExtension() {
 			return toString();
		};
	}

	public enum Document implements FileType {

		DOC;

		public String toString() {
			return "." + super.toString().toLowerCase();
		};

		@Override
		public String getExtension() {
 			return toString();
		};
	}

	public enum Text implements FileType {

		TXT;

		public String toString() {
			return "." + super.toString().toLowerCase();
		};

		@Override
		public String getExtension() {
 			return toString();
		};
	}

}