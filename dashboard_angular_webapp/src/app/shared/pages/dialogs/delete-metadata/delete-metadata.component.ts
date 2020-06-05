import { Component, OnInit , Inject} from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import {MAT_DIALOG_DATA, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {SensorService} from '../../../services/sensor.service';


@Component({
  selector: 'app-delete-metadata',
  templateUrl: './delete-metadata.component.html',
  styleUrls: ['./delete-metadata.component.css']
})
export class DeleteMetadataComponent implements OnInit {
  public delete = false;
  constructor(private dialogRef: MatDialogRef<DeleteMetadataComponent>) { }

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
