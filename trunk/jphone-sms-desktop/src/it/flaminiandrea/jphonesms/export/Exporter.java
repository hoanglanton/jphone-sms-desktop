package it.flaminiandrea.jphonesms.export;

import it.flaminiandrea.jphonesms.domain.Data;

public interface Exporter {
	public boolean export();
	public Data getData();
	public void setData(Data data);
	public String getPathToDirectory();
	public void setPathToDirectory(String pathToDirectory);
}
