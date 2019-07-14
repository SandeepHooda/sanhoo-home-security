package health.vo;

public class Device {
	private String _id;
	private String name;
	private long healthCheckTime;
	private boolean turnOnHealthCheck;
	private boolean alarmTriggered;
	private String alermNotificationText = "There is an suspicious activity on house number 55 Sector 27. Please call police to check.";
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getHealthCheckTime() {
		return healthCheckTime;
	}
	public void setHealthCheckTime(long healthCheckTime) {
		this.healthCheckTime = healthCheckTime;
	}
	public boolean isTurnOnHealthCheck() {
		return turnOnHealthCheck;
	}
	public void setTurnOnHealthCheck(boolean turnOnHealthCheck) {
		this.turnOnHealthCheck = turnOnHealthCheck;
	}
	public boolean isAlarmTriggered() {
		return alarmTriggered;
	}
	public void setAlarmTriggered(boolean alarmTriggered) {
		this.alarmTriggered = alarmTriggered;
	}
	public String getAlermNotificationText() {
		return alermNotificationText;
	}
	public void setAlermNotificationText(String alermNotificationText) {
		this.alermNotificationText = alermNotificationText;
	}

}
