import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Device } from '../domain/device';
import { IpAddress } from '../domain/ipAddress';

import { HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class DeviceService {
    private httpOptions = {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': ''+window.localStorage.getItem('regID')
        })
      };

    constructor(private http: HttpClient) {}

    getDevices(): Observable<Array<Device>>{
        return this.http.get<Array<Device>>('/Devices'
        //'/Devices' 'assets/data/devices.json'
            ,this.httpOptions);
    }

    getExternalIP(): Observable<IpAddress>{
      return this.http.get<IpAddress>('/MyExternalIP'
          ,this.httpOptions);
  }

    updateDevice(device:Device): Observable<Array<Device>> {
       return this.http.post<Array<Device>>('/Devices', device,this.httpOptions);
    }
}
