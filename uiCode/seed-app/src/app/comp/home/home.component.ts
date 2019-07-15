import { Component, OnInit } from '@angular/core';
import { Device } from './domain/device';

import {DeviceService} from './services/deviceservice'
import { IpAddress } from './domain/ipAddress';



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

   devices : Device[];
   cols: any[];
   externalIP :String;
  constructor(private deviceService: DeviceService) { 
      
  }

  ngOnInit() {
    
      this.cols = [
        { field: 'name', header: 'Name' },
        { field: 'alarmTriggered', header: 'Alarm' },
        { field: 'turnOnHealthCheck', header: 'Active/Passive' },
        { field: 'healthCheckTime', header: 'Last Hearbeat' }
    ];
    this.getDeviceStatus();
    this.getExternalIP();
     
  }

   refresh():void{
    this.getDeviceStatus();
    this.getExternalIP();
  }
  private  getExternalIP():void{
    this.deviceService.getExternalIP().subscribe(
      (ipAddress: IpAddress) => {
        let date :Date = new Date(ipAddress.time);
        this.externalIP = ipAddress.myIP+ date.toString() +" # "+ipAddress.time;
        }, error => {         
      }
    );
  }
  private  getDeviceStatus():void{

    this.deviceService.getDevices().subscribe(
      (devices: Array<Device>) => {
        this.getDisplayDate(devices);
        this.devices = devices
        }, error => {         
      }
    );
  }
  private  updateDevice(device:Device):void{
    
    this.deviceService.updateDevice(device).subscribe(
      (devices: Array<Device>) => {
        this.getDisplayDate(devices);
        this.devices = devices
        }, error => {         
      }
    );
  }
  private getDisplayDate(devices: Array<Device>):void{
    for (let i=0;i<devices.length;i++){
      let date :Date = new Date(devices[i].healthCheckTime);
      devices[i].healthCheckTimeDisplay = date.toString();
    }
    
  }
  private  moakDrill(_id:string):void{
    window.open("https://sanhoo-home-security.appspot.com/IamAlive?id="+_id+"&alarmTriggered=y");
  }
  
   
  
}

