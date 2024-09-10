import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { AssignmentService } from './assignment.service';

@Component({
  selector: 'app-assignment-upload',
  templateUrl: './assignment-upload.component.html',
  styleUrls: ['./assignment-upload.component.css']
})
export class AssignmentUploadComponent {
  assignmentId!: number;
  fileName!: string;
  deadline!: string;
  assignment: any = {}; // Store assignment details

  constructor(
    private route: ActivatedRoute,
    private assignmentService: AssignmentService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.assignmentId = +params['id']; // Get assignment ID from route
    });

    this.route.queryParams.subscribe(queryParams => {
      this.fileName = queryParams['fileName'];
      this.deadline = queryParams['deadline'];
      this.loadAssignmentDetails(this.assignmentId);
    });
  }

  loadAssignmentDetails(id: number) {
    this.assignmentService.getAssignmentById(id).subscribe((data: any) => {
      this.assignment = data;
      // Use this.fileName and this.deadline as needed
    });
  }

  viewAssignmentFile() {
    this.assignmentService.viewFileById(this.assignmentId).subscribe((response: Blob) => {
      const fileUrl = URL.createObjectURL(response);
      window.open(fileUrl);
    });
  }

  downloadAssignmentFile() {
    this.assignmentService.downloadFileById(this.assignmentId).subscribe((response: Blob) => {
      const fileUrl = URL.createObjectURL(response);
      const link = document.createElement('a');
      link.href = fileUrl;
      link.download = `assignment_${this.assignmentId}.pdf`;
      link.click();
    });
  }
}
