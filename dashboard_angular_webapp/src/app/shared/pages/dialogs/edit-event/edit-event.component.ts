import { Component, OnInit, Inject } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import {MAT_DIALOG_DATA, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {SensorService} from '../../../services/sensor.service';
import {Event} from '../../../model/event';


@Component({
  selector: 'app-edit-event',
  templateUrl: './edit-event.component.html',
  styleUrls: ['./edit-event.component.css']
})
export class EditEventComponent implements OnInit {
  public id: number;
  public eventTitle = '';
  public eventDescription = '';
  public startDate: Date;
  public endDate: Date;
  public selectedBuilding;
  public buildings = [];
  public buildingId = '';
  public clusters = [];
  public selectedCluster = null;
  public isGlobal = false;
  public errorMessage = '';
  
  constructor(private dialogRef: MatDialogRef<EditEventComponent>, @Inject(MAT_DIALOG_DATA) public data: any,
              private sensorService: SensorService) { 
    this.id = data.eventData.id;
    this.buildingId = data.eventData.buildingId;  
    this.eventTitle = data.eventData.title;
    this.eventDescription = data.eventData.description;
    this.startDate = data.eventData.start;
    this.endDate = data.eventData.end;
    this.clusters = data.eventData.cluster;
    this.buildings = data.buildings;
  }

  ngOnInit() {
  }
  save() {
    if (!this.validateTimes()) {
      this.errorMessage = 'End time cannot be before start time.';
    } else if (this.validateInput()) {
      let building = '';
      if (this.isGlobal) {
        building = 'GLOBAL';
      } else {
        this.buildingId = this.selectedBuilding.id;
      }

      // Convert our dates to Est time
      const estTimeStart = this.startDate.toLocaleString('en-US', {timeZone: 'America/New_York'});
      const convertedTimeStart = new Date(estTimeStart);

      let estTimeEnd = null;
      if (this.endDate) {
        estTimeEnd = this.endDate.toLocaleString('en-US', {timeZone: 'America/New_York'});  
      }
      const convertedTimeEnd = new Date(estTimeEnd);

      let cluster = null;
      if (this.selectedCluster) {
        cluster = this.selectedCluster.id;
      }

      const event: Event = {
        id: this.id,
        title: this.eventTitle,
        description: this.eventDescription,
        startDate: convertedTimeStart,
        endDate: convertedTimeEnd,
        buildingId: this.buildingId,
        clusterId: cluster
      };

      let clusterName = null;
      if (this.selectedCluster) {
        clusterName = this.selectedCluster.name;
      }

      const data = {
        eventData: event,
      };

      this.dialogRef.close(data);
    } else {
      this.errorMessage = 'Please enter all required information.';
    }
  }

  close() {
    this.dialogRef.close();
  }

  validateInput(): boolean {
    return !(this.eventTitle === '' || this.eventDescription === '' || (!this.isGlobal && !this.buildingId) || !this.startDate);
  }

  validateTimes(): boolean {

    if (!this.endDate) {
      return true;
    } else {
      const start = new Date(this.startDate);
      const end = new Date(this.endDate);
      return start.getTime() < end.getTime();
    }
  }

  selectBuilding($event) {
    this.sensorService.getClusters($event.value.id).subscribe((data) => {
      this.clusters = data;
    });
  }

}
