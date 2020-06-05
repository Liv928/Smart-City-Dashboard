import { Component, OnInit, Inject } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {SensorService} from '../../../services/sensor.service';

@Component({
  selector: 'app-edit-metadata',
  templateUrl: './edit-metadata.component.html',
  styleUrls: ['./edit-metadata.component.scss']
})
export class EditMetadataComponent implements OnInit {
  public id: number;
  public metaTitle = '';
  public metaDescription = '';
  public sensorID ='';
  public errorMessage = '';
  public originalTitle = '';
  public originalDescription = '';


  constructor(private dialogRef: MatDialogRef<EditMetadataComponent>, @Inject(MAT_DIALOG_DATA) public data: any) { 
    this.id = data.AddiMetadata.id;
    this.metaTitle = data.AddiMetadata.title;
    this.metaDescription = data.AddiMetadata.description;
    this.sensorID = data.AddiMetadata.sensorID;
    this.originalTitle = this.metaTitle;
    this.originalDescription = this.metaDescription;
  }

  ngOnInit() {
  }

  update() {
    if (this.metaTitle === '' && this.metaDescription === '') {
      this.errorMessage = 'Title and description must not be blank.';
    } else if (this.metaTitle === '') {
      this.errorMessage = 'Title must not be blank.';
    } else if (this.metaDescription === '') {
      this.errorMessage = 'Description must not be blank.';
    } else if (this.metaTitle === this.metaTitle || this.metaDescription === this.originalDescription){
      this.errorMessage = 'No changes made.';
    } else {
      console.log("edit id: " + this.id);
      const data = {id: this.id, metaTitle: this.metaTitle, metaDescription: this.metaDescription, sensorID: this.sensorID};
      this.dialogRef.close(data);
    }
  }

  close() {
    this.dialogRef.close();
  }

}
