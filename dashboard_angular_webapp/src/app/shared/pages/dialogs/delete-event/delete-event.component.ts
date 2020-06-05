import { Component, OnInit , Inject} from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import {MAT_DIALOG_DATA, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {SensorService} from '../../../services/sensor.service';
import {Event} from '../../../model/event';

@Component({
  selector: 'app-delete-event',
  templateUrl: './delete-event.component.html',
  styleUrls: ['./delete-event.component.css']
})
export class DeleteEventComponent implements OnInit {

  public delete = false;
  public isGlobal = false;
  public errorMessage = '';

  constructor(private dialogRef: MatDialogRef<DeleteEventComponent>, @Inject(MAT_DIALOG_DATA) public data: any,
              private sensorService: SensorService) {
               }

  ngOnInit() {
  }

  close() {
    this.dialogRef.close();
  }

  confirm(){
    this.delete = true;
    const data = {delete: this.delete};
    this.dialogRef.close(data);
  }


}
