package com.neu.snowhouse.ui.post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.snowhouse.R;
import com.neu.snowhouse.SessionManagement;
import com.neu.snowhouse.api.API;
import com.neu.snowhouse.api.RetrofitClient;
import com.neu.snowhouse.model.request.CommentRequestModel;
import com.neu.snowhouse.model.response.CommentResponseModel;
import com.neu.snowhouse.model.response.Image;
import com.neu.snowhouse.model.response.PostResponseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {
    String userName;
    int postId;
    API api = RetrofitClient.getInstance().getAPI();
    private final Handler textHandler = new Handler();
    ImageView image;
    TextView postTitle;
    TextView postContent;
    TextView postUserName;
    TextView postCreatedTime;
    TextView postLikeCount;
    TextView postDislikeCount;
    ImageButton thumbUp;
    ImageButton thumbDown;
    // for comments
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ArrayList<CommentResponseModel> comments = new ArrayList<>();
    // for add a new comment
    EditText newComment;
    Button addComment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = SessionManagement.getUserName(getContext());
        postId = getArguments().getInt("postId");
        image = view.findViewById(R.id.post_image);
        postTitle = view.findViewById(R.id.post_title);
        postContent = view.findViewById(R.id.post_content);
        postUserName = view.findViewById(R.id.post_userName);
        postCreatedTime = view.findViewById(R.id.post_createdTime);
        postLikeCount = view.findViewById(R.id.post_likeCount);
        postDislikeCount = view.findViewById(R.id.post_dislikeCount);
        thumbUp = view.findViewById(R.id.post_up);
        thumbDown = view.findViewById(R.id.post_down);
        newComment = view.findViewById(R.id.post_newComment);
        addComment = view.findViewById(R.id.button_newComment);
        thumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLikeStatus(api.likePost(postId, userName));
            }
        });
        thumbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLikeStatus(api.dislikePost(postId, userName));
            }
        });
        getPost();
        // for comments
        recyclerView = view.findViewById(R.id.post_comment_recycler_view);
        commentAdapter = new CommentAdapter(comments);
        getComments();
        LikeClickListener likeClickListener = new LikeClickListener() {
            @Override
            public void onIconClick(int position, String type) {
                Call<ResponseBody> changeCommentLikeStatus = null;
                int commentId = comments.get(position).getCommentId();
                if (type.equals("like")) {
                    changeCommentLikeStatus = api.likeComment(commentId, userName);
                } else if (type.equals("dislike")) {
                    changeCommentLikeStatus = api.dislikeComment(commentId, userName);
                }
                assert changeCommentLikeStatus != null;
                changeCommentLikeStatus.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            getComment(commentId, position);
                        }
                        if (!response.isSuccessful() && response.errorBody() != null) {
                            try {
                                Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        commentAdapter.setListener(likeClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commentAdapter);
        // for add a new comment
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = newComment.getText().toString().trim();
                if (!content.equals("")) {
                    CommentRequestModel commentRequestModel = new CommentRequestModel();
                    commentRequestModel.setUserName(userName);
                    commentRequestModel.setPostId(postId);
                    commentRequestModel.setContent(content);
                    addComment(commentRequestModel);
                }
            }
        });
    }

    private void getPost() {
        Call<PostResponseModel> fetchPost = api.getPostById(userName, postId);
        final Bitmap[] bmp = new Bitmap[1];
        fetchPost.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Thread thread = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            PostResponseModel postResponseModel = response.body();
                            if (postResponseModel.getImage() != null) {
                                Image image = postResponseModel.getImage();
                                byte[] bytes = Base64.getDecoder().decode(image.getPicture());
                                bmp[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            }
                            textHandler.post(() -> {
                                postTitle.setText(postResponseModel.getTitle());
                                postContent.setText(postResponseModel.getContent());
                                postUserName.setText(postResponseModel.getUserName());
                                postCreatedTime.setText(postResponseModel.getCreatedTime());
                                postLikeCount.setText(String.valueOf(postResponseModel.getLikeCount()));
                                postDislikeCount.setText(String.valueOf(postResponseModel.getDislikeCount()));
                                thumbUp.setColorFilter(Color.parseColor("#89000000"));
                                thumbDown.setColorFilter(Color.parseColor("#89000000"));
                                if (postResponseModel.isLikeClicked()) {
                                    thumbUp.setColorFilter(Color.parseColor("#000099"));
                                }
                                if (postResponseModel.isDislikeClicked()) {
                                    thumbDown.setColorFilter(Color.parseColor("#000099"));
                                }
                            });
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (bmp[0] != null) {
                        image.setImageBitmap(bmp[0]);
                        ViewGroup.LayoutParams params = image.getLayoutParams();
                        params.width = 362;
                        params.height = 221;
                        image.setLayoutParams(params);
                    }
                }
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeLikeStatus(Call<ResponseBody> call) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getPost();
                }
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getComments() {
        Call<List<CommentResponseModel>> fetchComments = api.getCommentsByPostId(postId, userName);
        fetchComments.enqueue(new Callback<List<CommentResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommentResponseModel>> call, Response<List<CommentResponseModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    comments = (ArrayList<CommentResponseModel>) response.body();
                    commentAdapter.updateAdapter(comments);
                }
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CommentResponseModel>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getComment(int commentId, int position) {
        Call<CommentResponseModel> fetchComment = api.getCommentById(commentId, userName);
        fetchComment.enqueue(new Callback<CommentResponseModel>() {
            @Override
            public void onResponse(Call<CommentResponseModel> call, Response<CommentResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommentResponseModel comment = response.body();
                    comments.set(position, comment);
                    commentAdapter.notifyItemChanged(position);
                }
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addComment(CommentRequestModel commentRequestModel) {
        Call<CommentResponseModel> addComment = api.addComment(commentRequestModel);
        addComment.enqueue(new Callback<CommentResponseModel>() {
            @Override
            public void onResponse(Call<CommentResponseModel> call, Response<CommentResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommentResponseModel comment = response.body();
                    comments.add(comment);
                    commentAdapter.notifyItemChanged(comments.size());
                    newComment.setText("");
                }
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}