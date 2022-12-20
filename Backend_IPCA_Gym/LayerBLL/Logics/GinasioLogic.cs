using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using System.Data.SqlClient;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    public class GinasioLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Ginasio> ginasioList = await GinasioService.GetAllService(sqlDataSource);

            if (ginasioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de ginásios obtida com sucesso!";
                response.Data = new JsonResult(ginasioList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Ginasio ginasio = await GinasioService.GetByIDService(sqlDataSource, targetID);

            if (ginasio != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Ginásio obtido com sucesso!";
                response.Data = new JsonResult(ginasio);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, Ginasio newGinasio)
        {
            Response response = new Response();
            bool creationResult = await GinasioService.PostService(sqlDataSource, newGinasio);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Ginasio criado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, Ginasio ginasio, int targetID)
        {
            Response response = new Response();
            bool updateResult = await GinasioService.PatchService(sqlDataSource, ginasio, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Ginasio alterado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await GinasioService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Ginasio apagado com sucesso!");
            }

            return response;
        }
    }
}
