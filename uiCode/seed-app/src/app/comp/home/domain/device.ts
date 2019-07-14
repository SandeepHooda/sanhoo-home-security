export interface Device {
    _id : string;
    name: string;
    healthCheckTime :number;
    turnOnHealthCheck:boolean;
    alarmTriggered:boolean;
    healthCheckTimeDisplay :string;
   
}
