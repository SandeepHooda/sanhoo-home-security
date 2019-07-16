export interface Device {
    _id : string;
    name: string;
    healthCheckTime :number;
    healthCheckTimeDisplay :string;
    lastAlarmTime : number;
    lastAlarmTimeDisplay :string;
    turnOnHealthCheck:boolean;
    alarmTriggered:boolean;
    unplugged:boolean;
   
}
