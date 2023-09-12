import { Component,Input,EventEmitter, Output  } from '@angular/core';
import { FileUploadService } from './file-upload.service';
import { CrudItemOptions } from 'app/shared/utils/crud-item-options/crud-item-options.model';
@Component({
	selector: 'app-file-upload',
	templateUrl: './file-upload.component.html',
	styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent<T>  {

	@Input() idProduct: number;

	// Variable to store shortLink from api response
	shortLink: string = "";
	loading: boolean = false; // Flag variable
	file: File = null; // Variable to store file

	// Inject service
	constructor(private fileUploadService: FileUploadService) { }

	// On file Select
	public onChange(event) {
		this.file = event.target.files[0];
	}
 
	// OnClick of button Upload
	public onUpload() {
		this.loading = !this.loading;
		this.fileUploadService.upload(this.file, this.idProduct).subscribe(
			(event: any) => {
				if (typeof (event) === 'object') {
					// Short link via api response
					this.shortLink = event.link;

					this.loading = false; // Flag variable
				}
			}
		);
	}
}
