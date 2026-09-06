import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDialogComponent } from '../../helpers/confirm-dialog/confirm-dialog.component';
import { AdminOpportunity, AdminPost } from '../../models/admin';
import { OpportunityStatus } from '../../models/opportunity';
import { AdminService } from '../../services/admin.service';

const OPPORTUNITY_STATUS_CLASSES: Record<OpportunityStatus, string> = {
  DRAFT: 'bg-gray-100 text-gray-500',
  OPEN: 'bg-green-light/10 text-green',
  FULL: 'bg-yellow-100 text-yellow-700',
  CLOSED: 'bg-gray-100 text-gray-500'
};

type ContentTab = 'posts' | 'opportunities';

/** Admin tab: moderate posts and opportunities platform-wide (delete only). */
@Component({
  selector: 'app-admin-content',
  templateUrl: './admin-content.component.html',
  styleUrls: ['./admin-content.component.css']
})
export class AdminContentComponent implements OnInit {
  activeTab: ContentTab = 'posts';

  posts: AdminPost[] = [];
  postsLoading = true;
  postsPage = 0;
  postsLast = true;

  opportunities: AdminOpportunity[] = [];
  opportunitiesLoading = true;

  constructor(private adminService: AdminService, private toastr: ToastrService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadPosts();
    this.loadOpportunities();
  }

  loadPosts(page = 0): void {
    this.postsLoading = true;
    this.adminService.listPosts(page).subscribe({
      next: (result) => {
        this.posts = page === 0 ? result.content : [...this.posts, ...result.content];
        this.postsPage = result.number;
        this.postsLast = result.last;
        this.postsLoading = false;
      },
      error: (err) => {
        console.error('Error loading posts:', err);
        this.toastr.error('Could not load posts.');
        this.postsLoading = false;
      }
    });
  }

  loadMorePosts(): void {
    if (!this.postsLast) this.loadPosts(this.postsPage + 1);
  }

  loadOpportunities(): void {
    this.opportunitiesLoading = true;
    this.adminService.listOpportunities().subscribe({
      next: (opportunities) => {
        this.opportunities = opportunities;
        this.opportunitiesLoading = false;
      },
      error: (err) => {
        console.error('Error loading opportunities:', err);
        this.toastr.error('Could not load opportunities.');
        this.opportunitiesLoading = false;
      }
    });
  }

  opportunityStatusClass(status: string): string {
    return OPPORTUNITY_STATUS_CLASSES[status as OpportunityStatus] ?? 'bg-gray-100 text-gray-500';
  }

  deletePost(post: AdminPost): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '380px',
      data: {
        title: 'Delete post?',
        message: `Delete this post by ${post.authorName}? This cannot be undone.`,
        confirmLabel: 'Delete',
        destructive: true
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) return;

      this.adminService.deletePost(post.id).subscribe({
        next: () => {
          this.posts = this.posts.filter((p) => p.id !== post.id);
          this.toastr.success('Post deleted.');
        },
        error: (err) => {
          console.error('Error deleting post:', err);
          this.toastr.error('Could not delete this post.');
        }
      });
    });
  }

  deleteOpportunity(opportunity: AdminOpportunity): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '380px',
      data: {
        title: 'Delete opportunity?',
        message: `Delete "${opportunity.title}"? This cannot be undone.`,
        confirmLabel: 'Delete',
        destructive: true
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) return;

      this.adminService.deleteOpportunity(opportunity.id).subscribe({
        next: () => {
          this.opportunities = this.opportunities.filter((o) => o.id !== opportunity.id);
          this.toastr.success('Opportunity deleted.');
        },
        error: (err) => {
          console.error('Error deleting opportunity:', err);
          this.toastr.error('Could not delete this opportunity.');
        }
      });
    });
  }
}
