import { Component, OnInit } from '@angular/core';

import {DeviceService} from '../home/services/deviceservice'

import {DeleteAll_VO} from '../home/domain/DeleteAll_VO';
@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  deletefoscamInboxDaily : boolean;
  constructor(private deviceService: DeviceService) { 
      
  }

  ngOnInit() {
    
    this.getEmailAutoDeleteStatus();
   }

   public getEmailAutoDeleteStatus():void{
    this.deviceService.getEmailAutoDeleteStatus().subscribe(
      (status: DeleteAll_VO) => {
        this.deletefoscamInboxDaily = status.deleteAll;
        }, error => {         
      }
    );
  }

  public toggleEmailDeletePrcocess():void{
    this.deviceService.toggleEmailDeletePrcocess().subscribe(
      (status: DeleteAll_VO) => {
        this.deletefoscamInboxDaily = status.deleteAll;
        }, error => {         
      }
    );
  }
}
