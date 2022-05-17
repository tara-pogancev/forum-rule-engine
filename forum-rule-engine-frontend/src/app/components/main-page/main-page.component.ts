import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Post } from 'src/app/model/post';
import { RulesResponse } from 'src/app/model/rules-response';
import { User } from 'src/app/model/user';
import { PostServiceService as PostService } from 'src/app/service/post.service';
import { UserServiceService as UserService } from 'src/app/service/user.service';
import { HelpDialogComponent } from '../help-dialog/help-dialog.component';

@Component({
  selector: 'main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss'],
})
export class MainPageComponent implements OnInit {
  users: User[] = [];
  posts: Post[] = [];
  logs: RulesResponse[] = [];
  activeUsername: string = 'Genesis';
  newPostContent: string = '';

  constructor(
    public dialog: MatDialog,
    private postService: PostService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.refreshAllPosts();
    this.refreshAllUsers();

    this.addLog('FORUM RULE ENGINE PLAYGROUND CONSOLE IS READY.');
  }

  refreshAllUsers() {
    this.userService.getAll().subscribe((data) => {
      this.users = data;
    });
  }

  refreshAllPosts() {
    this.postService.getAll().subscribe((data) => {
      data.sort((a: { timestamp: number }, b: { timestamp: number }) =>
        a.timestamp < b.timestamp ? 1 : -1
      );
      this.posts = data;
    });
  }

  openHelpDialog() {
    const dialogRef = this.dialog.open(HelpDialogComponent);
  }

  isUserSuspended(user: User) {
    return (
      user.userLabels.indexOf('SUSPENDED') > -1 ||
      user.userLabels.indexOf('TEMPORARILY_SUSPENDED') > -1
    );
  }

  isActiveUserSuspended() {
    return this.isUserSuspended(this.getUserFromListById(this.activeUsername));
  }

  getUserFromListById(id: String): User {
    return this.users.filter((u) => {
      return u.username == id;
    })[0];
  }

  getPostFromListById(id: String): Post {
    return this.posts.filter((p) => {
      return p.postId == id;
    })[0];
  }

  likePost(postId: String) {
    if (this.isActiveUserSuspended()) {
      alert('Current active user is suspended.');
    } else {
      this.postService
        .likePost(this.activeUsername, postId)
        .subscribe((data) => {
          if (data.message != null) {
            this.addLog(data.message);
          }
          this.getPostFromListById(postId).likes =
            this.getPostFromListById(postId).likes + 1;
        });
    }
  }

  dislikePost(postId: String) {
    if (this.isActiveUserSuspended()) {
      alert('Current active user is suspended.');
    } else {
      this.postService
        .dislikePost(this.activeUsername, postId)
        .subscribe((data) => {
          if (data.message != null) {
            this.addLog(data.message);
          }
          this.getPostFromListById(postId).dislikes =
            this.getPostFromListById(postId).dislikes + 1;
        });
    }
  }

  reportPost(postId: String) {
    if (this.isActiveUserSuspended()) {
      alert('Current active user is suspended.');
    } else {
      this.postService
        .reportPost(this.activeUsername, postId)
        .subscribe((data) => {
          if (data.message != null) {
            this.addLog(data.message);
          }
          this.getPostFromListById(postId).reports =
            this.getPostFromListById(postId).reports + 1;
        });
    }
  }

  createPost() {
    if (this.isActiveUserSuspended()) {
      alert('Current active user is suspended.');
    } else {
      var newPost = new Post();
      newPost.postOwnerId = this.activeUsername;
      newPost.postContent = this.newPostContent;
      this.postService.createPost(newPost).subscribe((data) => {
        this.refreshAllPosts();
        if (data.message != null) {
          this.addLog(data.message);
        }
      });
    }
  }

  addLog(message: string) {
    this.logs.push(new RulesResponse(message));
    var millisecondsToWait = 500;
    /*setTimeout( () => {
      this.updateScroll();
    }, millisecondsToWait); */
  }

  updateScroll() {
    var element = document.getElementById('log-terminal')!;
    element.scrollTop = element.scrollHeight;
  }
}
